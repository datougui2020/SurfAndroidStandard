/*
 * Copyright (c) 2019-present, SurfStudio LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.surfstudio.android.core.mvp.binding.rx.relation.mvp

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvp.binding.rx.relation.Relation

/**
 *  Отношение Presenter -> View
 *
 *  Еммит единичное событие.
 *  В отличии от [State] не эммитит последне значение при подписке
 */
class Command<T> : Relation<T, PRESENTER, VIEW>() {

    private val relay = PublishRelay.create<T>()

    override fun getConsumer(source: PRESENTER): Consumer<T> = relay

    override fun getObservable(target: VIEW): Observable<T> = relay.share()
}