package com.github.regyl.gfi.service.impl.other;

import com.github.regyl.gfi.model.LabelModel;
import com.github.regyl.gfi.service.other.LabelService;
import com.github.regyl.gfi.util.ResourceUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

@Component
public class LabelServiceImpl implements LabelService {

    private static final Collection<LabelModel> DATA;

    static {
        DATA = Arrays.stream(ResourceUtil.getFilePayload("data/labels.txt").split("\r\n"))
                .map(LabelModel::new)
                .toList();
    }

    @Override
    public Collection<LabelModel> findAll() {
        return DATA;
    }
}
