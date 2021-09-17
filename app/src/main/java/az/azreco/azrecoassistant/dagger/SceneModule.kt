package az.azreco.azrecoassistant.dagger

import az.azreco.azrecoassistant.scene.SceneContainer
import az.azreco.azrecoassistant.scene.scenes.SmsScene
import az.azreco.azrecoassistant.scene.scenes.WeatherScene
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object SceneModule {

}