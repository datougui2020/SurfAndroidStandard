package ru.surfstudio.standard.app_injector.ui.notification


import dagger.Component
import ru.surfstudio.standard.i_push_notification.storage.FcmStorage
import ru.surfstudio.standard.app_injector.AppComponent
import javax.inject.Scope

@PerService
@Component(dependencies = [AppComponent::class])
interface FirebaseServiceComponent {

    fun fcmStorage(): FcmStorage

    fun inject(s: MessagingService)
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerService