package com.github.regyl.gfi.service.impl.feed.cyclonedx;

import com.github.regyl.gfi.model.SbomModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class CycloneDxResultConsumerImpl implements BiConsumer<SbomModel, Throwable> {

    private final Consumer<SbomModel> modelConsumer;
    private final Consumer<Throwable> exceptionConsumer;

    @Override
    public void accept(SbomModel model, Throwable throwable) {
        if (throwable != null) {
            exceptionConsumer.accept(throwable);
        } else {
            modelConsumer.accept(model);
        }
    }
}
