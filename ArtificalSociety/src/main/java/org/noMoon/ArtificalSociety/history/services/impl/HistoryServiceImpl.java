package org.noMoon.ArtificalSociety.history.services.impl;

import org.noMoon.ArtificalSociety.history.DAO.HistoryMapper;
import org.noMoon.ArtificalSociety.history.DO.History;
import org.noMoon.ArtificalSociety.history.DTO.HistoryDTO;
import org.noMoon.ArtificalSociety.history.services.HistoryService;

/**
 * Created by noMoon on 2015-10-16.
 */
public class HistoryServiceImpl implements HistoryService {

    HistoryMapper historyMapper;

    public Long insertNewHistoryDTO(HistoryDTO historyDTO) {
        History insertDO=historyDTO.convertToHistory();
        historyMapper.insert(insertDO);
        historyDTO.setId(insertDO.getId());
        return insertDO.getId();
    }

    public void setHistoryMapper(HistoryMapper historyMapper) {
        this.historyMapper = historyMapper;
    }
}
