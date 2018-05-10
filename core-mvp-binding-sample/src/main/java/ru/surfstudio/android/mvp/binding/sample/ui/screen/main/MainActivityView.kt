package ru.surfstudio.android.mvp.binding.sample.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.IdRes
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import ru.surfstudio.android.core.mvp.binding.BaseBindableActivityView
import ru.surfstudio.android.core.mvp.binding.BindData
import ru.surfstudio.android.core.mvp.binding.sample.R
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.mvp.binding.sample.ui.base.configurator.ActivityScreenConfigurator
import ru.surfstudio.android.mvp.binding.sample.ui.screen.main.view.PaneView
import javax.inject.Inject

/**
 * Вью главного экрана.
 * Демонстрирует работу с [BindData]. Здесь непорядок с подходом. Но, если тема взлетит обещаю исправить
 * @author Vasily Beglyanin
 */
class MainActivityView : BaseBindableActivityView<MainScreenModel>() {

    override fun onBind(screenModel: MainScreenModel) {

        screenModel.solved.observe(this) {
            if (it) {
                toast("You win")
            } else {
                toast("You broke it")
            }
        }

        screenModel.panel1.observeAndApply(this) { observePane(pane_1, it) }
        screenModel.panel2.observeAndApply(this) { observePane(pane_2, it) }
        screenModel.panel3.observeAndApply(this) { observePane(pane_3, it) }
        screenModel.panel4.observeAndApply(this) { observePane(pane_4, it) }
        screenModel.panel5.observeAndApply(this) { observePane(pane_5, it) }
        screenModel.panel6.observeAndApply(this) { observePane(pane_6, it) }
        screenModel.panel7.observeAndApply(this) { observePane(pane_7, it) }
        screenModel.panel8.observeAndApply(this) { observePane(pane_8, it) }
        screenModel.panel9.observeAndApply(this) { observePane(pane_9, it) }

        pane_1.listener = { listenPane(screenModel.panel1, it.toInt()) }
        pane_2.listener = { listenPane(screenModel.panel2, it.toInt()) }
        pane_3.listener = { listenPane(screenModel.panel3, it.toInt()) }
        pane_4.listener = { listenPane(screenModel.panel4, it.toInt()) }
        pane_5.listener = { listenPane(screenModel.panel5, it.toInt()) }
        pane_6.listener = { listenPane(screenModel.panel6, it.toInt()) }
        pane_7.listener = { listenPane(screenModel.panel7, it.toInt()) }
        pane_8.listener = { listenPane(screenModel.panel8, it.toInt()) }
        pane_9.listener = { listenPane(screenModel.panel9, it.toInt()) }
    }

    private fun observePane(pane: PaneView, data: PaneDataModel) {
        with(pane) {
            text = data.value.toString()
            when (data.state) {
                State.PRESSED -> setPressed()
                State.UNPRESSED -> setUnpressed()
            }
        }
    }

    private fun listenPane(pane: BindData<PaneDataModel>, data: Int) {
        pane.setValue(this, pane.value.copy(value = data))
    }

    @Inject
    internal lateinit var presenter: MainPresenter

    @IdRes
    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    override fun getScreenName(): String {
        return "main"
    }

    override fun getPresenters(): Array<CorePresenter<*>> {
        return arrayOf(presenter)
    }

    override fun createConfigurator(): ActivityScreenConfigurator {
        return MainScreenConfigurator(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?, viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        easy_win_btn.setOnClickListener { presenter.onEasyWinClick() }
    }
}
