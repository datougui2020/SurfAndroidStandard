package ru.surfstudio.android.navigation.command.activity

import android.os.Bundle
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.resource.EmptyResourceAnimations
import ru.surfstudio.android.navigation.command.activity.base.ActivityNavigationCommand
import ru.surfstudio.android.navigation.route.activity.ActivityRoute

data class Replace(
        override val route: ActivityRoute,
        override val animations: Animations = EmptyResourceAnimations,
        val activityOptions: Bundle? = null
) : ActivityNavigationCommand