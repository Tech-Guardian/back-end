import base64
import json
import sys
import cv2
from flask_cors import CORS
import requests
from ultralytics import YOLO
import os
from flask import Flask, render_template, Response
from datetime import datetime, timedelta
import numpy as np
from flask_socketio import SocketIO
import threading
import time

app = Flask(__name__, template_folder='template')
CORS(app, resources={r"/*": {"origins": "*"}})  # Permitir todas as origens
socketio = SocketIO(app, cors_allowed_origins="*")

model_path = os.path.abspath("ml/python/models/yolov5su.pt")
model = YOLO(model_path)

# Variáveis para detecção de pessoas com OpenCV e YOLO
pessoas_dict = {}
direcao_tempo_dict = {}
ids_disponiveis = []
ultimo_tempo_detecao = {}
limiar_similaridade = 150  # Ajuste conforme necessário

tempo_minimo_entrada_saida = timedelta(seconds=0.3)
tempo_minimo_reutilizacao_id = timedelta(seconds=2)

porta = 5000

# Adicione um contador de frames no início do seu script
frame_count = 0

# Defina a taxa de envio de frames processados
taxa_envio_frames = 16  # Frames por segundo

# Frame buffer para armazenar o último frame processado
frame_buffer = None

# Função para processar um frame de vídeo
def processar_video(frame):
    global pessoas_dict, direcao_tempo_dict, ids_disponiveis, ultimo_tempo_detecao, frame_buffer
    if frame is None:
        return None

    frame = cv2.resize(frame, (640, 480))

    # Obter resultados do modelo
    results = model(frame, classes=[0], stream=True)

    # IDs das pessoas no quadro atual
    ids_presentes = []

    # Processar cada detecção
    for result in results:
        boxes = result.boxes.xyxy.cpu().numpy()
        for detection in boxes:
            x1, y1, x2, y2 = detection

            # Encontrar uma pessoa existente ou atribuir um novo ID
            id_pessoa = None
            for pessoa_id, (px1, py1, px2, py2) in pessoas_dict.items():
                # Verificar se a pessoa atual está próxima o suficiente da pessoa já rastreada
                if (abs(x1 - px1) < limiar_similaridade and
                    abs(y1 - py1) < limiar_similaridade and
                    abs(x2 - px2) < limiar_similaridade and
                    abs(y2 - py2) < limiar_similaridade):
                    id_pessoa = pessoa_id
                    break

            # Se não encontrar uma pessoa próxima, atribuir um novo ID
            if id_pessoa is None:
                if ids_disponiveis:
                    id_pessoa = min(ids_disponiveis)
                    ids_disponiveis.remove(id_pessoa)
                else:
                    id_pessoa = max(pessoas_dict.keys()) + 1 if pessoas_dict else 1

                # Registrar a direção e o tempo da pessoa
                if x1 < 10:  # Pessoa surgindo pela esquerda
                    direcao_tempo_dict[id_pessoa] = ('esquerda', datetime.now())
                elif x2 > frame.shape[1] - 10:  # Pessoa surgindo pela direita
                    direcao_tempo_dict[id_pessoa] = ('direita', datetime.now())

            # Atualizar o dicionário de pessoas com o ID atual
            pessoas_dict[id_pessoa] = (x1, y1, x2, y2)
            ids_presentes.append(id_pessoa)
            ultimo_tempo_detecao[id_pessoa] = datetime.now()

            # Desenhar retângulo e ID da pessoa
            cv2.rectangle(frame, (int(x1), int(y1)), (int(x2), int(y2)), (0, 255, 0), 2)
            cv2.putText(frame, f'ID: {id_pessoa}', (int(x1), int(y1) - 5), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 1, cv2.LINE_AA)

    # Verificar se algum ID não está mais presente
    for id in list(pessoas_dict.keys()):
        if id not in ids_presentes:
            if id in direcao_tempo_dict:
                direcao, tempo = direcao_tempo_dict[id]
                if id in pessoas_dict:
                    if direcao == 'esquerda' and pessoas_dict[id][2] > frame.shape[1] - 10:  # Pessoa saindo pela direita
                        if datetime.now() - tempo > tempo_minimo_entrada_saida:
                            data_saida = {"dataSaida": tempo.strftime("%d-%m-%Y"), "horaSaida": datetime.now().strftime("%H:%M:%S"), "quantSaida": 1, "obsSaida": "Observacao, se houver"}
                            response_saida = requests.post('http://localhost:8080/saida', json=data_saida)
                            print("Dados de saída:")
                            print(json.dumps(data_saida, indent=4))
                    elif direcao == 'direita' and pessoas_dict[id][0] < 10:  # Pessoa saindo pela esquerda
                        if datetime.now() - tempo > tempo_minimo_entrada_saida:
                            data_entrada = {"dataEntrada": tempo.strftime("%d-%m-%Y"), "horaEntrada": datetime.now().strftime("%H:%M:%S"), "quantEntrada": 1, "obsEntrada": "Observacao, se houver"}
                            response_entrada = requests.post('http://localhost:8080/entrada', json=data_entrada)
                            print("Dados de entrada:")
                            print(json.dumps(data_entrada, indent=4))
                if id in direcao_tempo_dict:
                    del direcao_tempo_dict[id]
            if id in pessoas_dict:  # Verificar se o ID ainda está presente antes de tentar deletá-lo
                ids_disponiveis.append(id)
                del pessoas_dict[id]

    # Liberar IDs para reutilização após tempo mínimo
    for id, tempo_detecao in list(ultimo_tempo_detecao.items()):
        if datetime.now() - tempo_detecao > tempo_minimo_reutilizacao_id:
            if id in pessoas_dict:
                ids_disponiveis.append(id)
                del pessoas_dict[id]
            del ultimo_tempo_detecao[id]

    # Exibir o número de pessoas na imagem
    cv2.putText(frame, f"Pessoas: {len(pessoas_dict)}", (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 255), 2)

    # Atualiza o frame buffer com o frame processado
    frame_buffer = frame

    print("PROCESSADO")
    return frame

# Função para processar e enviar frames para o cliente
def enviar_frames_processados():
    global frame_count

    while True:
        frame_count += 1
        print(f"Processando o frame {frame_count}")

        # Captura o frame da câmera
        success, frame = camera.read()
        if not success:
            print("Erro ao capturar o frame.")
            break

        if frame_count % (30 // taxa_envio_frames) == 0:  # Ajusta a taxa de envio
            processed_frame = processar_video(frame)  # processa o frame

            if processed_frame is not None:
                ret, buffer = cv2.imencode('.png', processed_frame)  # Alterado o formato para PNG
                if ret:
                    processed_frame_bytes = base64.b64encode(buffer)
                    socketio.emit('processed_frame', processed_frame_bytes.decode('utf-8'))

            # Aguarda um tempo para evitar o processamento excessivo
            time.sleep(1 / taxa_envio_frames)

# Função geradora para retornar o stream da webcam
def gerar_stream():
    while True:
        success, frame = camera.read()
        if not success:
            break
        ret, buffer = cv2.imencode('.jpg', frame)
        frame = buffer.tobytes()
        yield (b'--frame\r\n'
               b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n')

# Rota para transmitir o vídeo resultante dos frames processados
@app.route('/')
def index():
    return render_template('index.html')

# Rota para retornar frames processados
@app.route('/frames')
def get_frames():
    def generate():
        while True:
            if frame_buffer is not None:
                ret, buffer = cv2.imencode('.png', frame_buffer)
                frame = buffer.tobytes()
                yield (b'--frame\r\n'
                       b'Content-Type: image/png\r\n\r\n' + frame + b'\r\n')
            time.sleep(1 / taxa_envio_frames)
    return Response(generate(), mimetype='text/event-stream')

# Rota para retornar o stream da webcam
@app.route('/stream')
def stream():
    return Response(gerar_stream(), mimetype='multipart/x-mixed-replace; boundary=frame')

@socketio.on('image')
def receive_image(base64_image):
    image_data = base64.b64decode(base64_image.split(',')[1])
    image_data = np.frombuffer(image_data, dtype=np.uint8)
    frame = cv2.imdecode(image_data, cv2.IMREAD_COLOR)

    print("FRAME A PROCESSAR")

    # Chama a função para enviar frames processados
    enviar_frames_processados()

if __name__ == '__main__':

    # Inicia a câmera
    camera = cv2.VideoCapture(0)
    if not camera.isOpened():
        print("Erro ao abrir a câmera.")
        sys.exit()

    # Inicia a thread para enviar frames processados
    threading.Thread(target=enviar_frames_processados, daemon=True).start()
    socketio.run(app, host='0.0.0.0', port=porta)
