import cv2
import requests
from datetime import datetime, timedelta
import json
from ultralytics import YOLO

# Carregar o modelo YOLOv8 com ultralytics
# model_path = "ml\python\models\yolov5su.pt"
model_path = "ml/python/models/yolov5su.pt" #Para o GITPOD
model = YOLO(model_path)  # Carregar o modelo

# Iniciar a captura de vídeo
cap = cv2.VideoCapture(0)
pessoas_dict = {}
direcao_tempo_dict = {}
ids_disponiveis = []
id_pessoa = 0

# Limiar de similaridade para considerar a mesma pessoa
limiar_similaridade = 150  # Ajuste conforme necessário

# Tempo mínimo entre a entrada e saída de uma pessoa
tempo_minimo_entrada_saida = timedelta(seconds=0.3)

# Tempo mínimo para reutilização de IDs
tempo_minimo_reutilizacao_id = timedelta(seconds=2)

# Último tempo de detecção de cada ID
ultimo_tempo_detecao = {}

# Loop principal
while True:
    ret, frame = cap.read()
    if not ret:
        break

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
                del direcao_tempo_dict[id]
            ids_disponiveis.append(id)
            del pessoas_dict[id]

    # Liberar IDs para reutilização após tempo mínimo
    for id, tempo_detecao in list(ultimo_tempo_detecao.items()):
        if datetime.now() - tempo_detecao > tempo_minimo_reutilizacao_id:
            ids_disponiveis.append(id)
            del pessoas_dict[id]
            del ultimo_tempo_detecao[id]

    # Exibir o número de pessoas na imagem
    cv2.putText(frame, f"Pessoas: {len(pessoas_dict)}", (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 255), 2)

    # Mostrar o frame
    cv2.imshow("Detecção de Pessoas", frame)

    # Sair com 'q'
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

# Liberar recursos
cap.release()
cv2.destroyAllWindows()
