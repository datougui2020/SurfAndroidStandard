package ru.surfstudio.android.navigation.executor.fragment

import ru.surfstudio.android.navigation.command.fragment.*
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand
import ru.surfstudio.android.navigation.di.supplier.FragmentNavigationSupplier
import ru.surfstudio.android.navigation.di.supplier.error.SupplierNotInitializedError
import ru.surfstudio.android.navigation.executor.CommandExecutor
import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigatorInterface
import ru.surfstudio.android.navigation.route.tab.TabRoute

open class FragmentCommandExecutor(
        private val fragmentNavigationSupplier: FragmentNavigationSupplier
) : CommandExecutor<FragmentNavigationCommand> {

    private val fragmentNavigator: FragmentNavigatorInterface
        get() = fragmentNavigationSupplier.currentHolder?.fragmentNavigator
                ?: throw SupplierNotInitializedError()

    private val tabFragmentNavigator: FragmentNavigatorInterface
        get() = fragmentNavigationSupplier.currentHolder?.fragmentNavigator
                ?: throw SupplierNotInitializedError()

    override fun execute(command: FragmentNavigationCommand) {
        when (command.route) {
            is TabRoute -> execute(command, tabFragmentNavigator)
            else -> execute(command, fragmentNavigator)
        }
    }

    protected open fun execute(command: FragmentNavigationCommand, navigator: FragmentNavigatorInterface) {
        when (command) {
            is Add -> navigator.add(command.route, command.animations)
            is Replace -> navigator.replace(command.route, command.animations)
            is ReplaceHard -> navigator.replaceHard(command.route, command.animations)
            is Remove -> navigator.remove(command.route, command.animations)
            is RemoveLast -> navigator.removeLast(command.animations)
            is RemoveUntil -> navigator.removeUntil(command.route, command.isInclusive)
            is RemoveAll -> navigator.removeAll()
        }
    }
}