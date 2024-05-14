package techguardian.api.interfaces;

import java.util.List;

import techguardian.api.entity.Input;

public interface IInputService {

    List<Input> findAll();
    Input createInput(Input createdInput);
    Input updateInput(Long id, Input updatedInput);
    Input deleteInput (Long id);

}
