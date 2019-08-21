/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.core.mvp.model;

import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface;
import ru.surfstudio.android.core.mvp.model.state.SwipeRefreshState;

/**
 * модель экрана с поддержкой
 * {@link LoadStateInterface}
 * {@link SwipeRefreshState}
 * <p>
 * работает в связке c BaseLdsSwr...View
 * В случае изменения LoadState, SwipeRefreshState устанавливается в SwipeRefreshState.HIDE
 * <p>
 * также см {@link ScreenModel}
 */
public class LdsSwrScreenModel extends LdsScreenModel {
    private SwipeRefreshState swipeRefreshState = SwipeRefreshState.HIDE;

    public SwipeRefreshState getSwipeRefreshState() {
        return swipeRefreshState;
    }

    public void setSwipeRefreshState(SwipeRefreshState swipeRefreshState) {
        this.swipeRefreshState = swipeRefreshState;
    }

    @Override
    public void setLoadState(LoadStateInterface loadState) {
        //В случае изменения LoadState, SwipeRefreshState устанавливается в SwipeRefreshState.HIDE
        setSwipeRefreshState(SwipeRefreshState.HIDE);
        super.setLoadState(loadState);
    }
}