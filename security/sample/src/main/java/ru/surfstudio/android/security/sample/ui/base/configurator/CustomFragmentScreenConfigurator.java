package ru.surfstudio.android.security.sample.ui.base.configurator;

import android.os.Bundle;

import ru.surfstudio.android.core.mvp.configurator.BaseFragmentViewConfigurator;
import ru.surfstudio.android.core.ui.activity.CoreActivityInterface;
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultFragmentScreenModule;
import ru.surfstudio.android.security.sample.ui.base.dagger.activity.CustomActivityComponent;

/**
 * Базовый конфигуратор для экрана, основанного на фрагменте
 */
public abstract class CustomFragmentScreenConfigurator
        extends BaseFragmentViewConfigurator<CustomActivityComponent, DefaultFragmentScreenModule> {

    public CustomFragmentScreenConfigurator(Bundle args) {
        super(args);
    }

    @Override
    protected DefaultFragmentScreenModule getFragmentScreenModule() {
        return new DefaultFragmentScreenModule(getPersistentScope());
    }

    @Override
    protected CustomActivityComponent getParentComponent() {
        return (CustomActivityComponent) ((CoreActivityInterface) getTargetFragmentView().getActivity())
                .getPersistentScope()
                .getConfigurator()
                .getActivityComponent();
    }
}
