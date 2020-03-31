package ru.surfstudio.android.navigation.di

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import ru.surfstudio.android.navigation.navigator.fragment.tab.view.ViewTabFragmentNavigator
import ru.surfstudio.android.navigation.navigator.fragment.view.ViewFragmentNavigator

//@PerAct
class FragmentNavigationSupplier : FragmentManager.FragmentLifecycleCallbacks() {

    /**
     * Текущий уровень вложенности навигации фрагментов
     * 0-й уровень соответствует supportFragmentManager'у родительской Activity,
     * 1-й уровень соответствует отображению внутри навигаторов
     */
    var currentLevel = 0
    private val navigatorHolders = hashMapOf<Int, MutableList<FragmentNavigatorHolder>>()
    private var currentId: String? = null


    override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        if (navigatorHolders.isEmpty()) {
            addZeroLevelHolder(f.requireActivity(), savedInstanceState)
        }
        val level = getLevel(f)
        val id = getFragmentId(f)
        val containerId = getContainerId(f) ?: return
        addHolder(level, containerId, id, fm, savedInstanceState)
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        val id = getFragmentId(f)
        val currentLevelHolders = navigatorHolders[currentLevel]
        val existsOnCurrentLevel = currentLevelHolders?.find { it.id == id } != null
        if (existsOnCurrentLevel) {
            currentId = id
        }
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        val id = getFragmentId(f)
        if (id == currentId) {
            currentId = null
        }
    }


    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        val id = getFragmentId(f)

        navigatorHolders.forEach { entry ->
            val currentHolder = entry.value.find { it.id == id }
            currentHolder?.run {
                fragmentNavigator.onSaveState(outState)
                tabFragmentNavigator.onSaveState(outState)
            }
        }
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        removeHolder(f)
    }

    private fun removeHolder(fragment: Fragment) {
        val id = getFragmentId(fragment)
        navigatorHolders.forEach { entry ->
            val holder = entry.value.find { it.id == id }
            entry.value.remove(holder)
        }
    }

    private fun getLevel(fragment: Fragment): Int {
        var level = 1
        var currentFragment: Fragment? = fragment
        while (currentFragment?.parentFragment != null) {
            level++
            currentFragment = currentFragment.parentFragment
        }
        return level
    }

    /**
     * Добавление холдера с навигаторами в список холдеров на определенный уровень
     *
     * @param level уровень вложенности, на который будет добавлен холдер
     * @param containerId идентификатор контейнера, в котором будет происходить навигация
     * @param id уникальный идентификатор фрагмента
     * @param fm менеджер, который будет осуществлять навигацию
     * @param savedInstanceState состояние, используемое для восстановления бекстека
     */
    private fun addHolder(
            level: Int,
            containerId: Int,
            id: String,
            fm: FragmentManager,
            savedInstanceState: Bundle?
    ) {
        val fragmentNavigator = ViewFragmentNavigator(fm, containerId, savedInstanceState)
        val tabFragmentNavigator = ViewTabFragmentNavigator(fm, containerId, savedInstanceState)
        val newHolder = FragmentNavigatorHolder(id, fragmentNavigator, tabFragmentNavigator)
        val levelHolders = navigatorHolders[level] ?: mutableListOf()
        levelHolders.add(newHolder)
        navigatorHolders[level] = levelHolders
    }

    /**
     * Добавление холдера на 0-ой уровень, т.е. на уровень Activity, которая управляет фрагментами.
     */
    private fun addZeroLevelHolder(activity: FragmentActivity, savedInstanceState: Bundle?) {
        if (activity !is FragmentContainer) return

        val fragmentManager = activity.supportFragmentManager
        val containerId = activity.containerId

        addHolder(0, containerId, ZERO_CONTAINER_ID, fragmentManager, savedInstanceState)
    }

    private fun getFragmentId(fragment: Fragment): String {
        return if (fragment is IdentifiableScreen) fragment.screenId else fragment.id.toString()
    }

    private fun getContainerId(fragment: Fragment): Int? {
        return if (fragment is FragmentContainer) fragment.containerId else null
    }

    private companion object {
        const val ZERO_CONTAINER_ID = "zero_container"
    }
}