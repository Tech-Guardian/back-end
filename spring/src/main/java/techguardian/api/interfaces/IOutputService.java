package techguardian.api.interfaces;

import java.util.List;

import techguardian.api.entity.Output;

public interface IOutputService {

    List<Output> findAll();
    Output createOutput(Output createdOutput);
    Output updateOutput(Long id, Output updatedOutput);
    Output deleteOutput (Long id);
}
