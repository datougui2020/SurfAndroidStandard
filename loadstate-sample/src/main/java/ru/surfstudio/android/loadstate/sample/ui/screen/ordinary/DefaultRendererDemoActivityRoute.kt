package ru.surfstudio.android.loadstate.sample.ui.screen.ordinary

import android.content.Context
import android.content.Intent
import ru.surfstudio.android.core.ui.navigation.activity.route.ActivityRoute

/**
 * Роут экрана для демонстрации работы DefaultLoadStateRenderer
 */
class DefaultRendererDemoActivityRoute : ActivityRoute() {
    override fun prepareIntent(context: Context): Intent {
        return Intent(context, DefaultRendererDemoActivityView::class.java)
    }
}
