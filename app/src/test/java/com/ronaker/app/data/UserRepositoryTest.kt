package com.ronaker.app.data

import com.ronaker.app.RxImmediateSchedulerRule
import com.ronaker.app.base.Result
import com.ronaker.app.data.local.FakePreferencesProvider
import com.ronaker.app.data.local.PreferencesDataSource
import com.ronaker.app.data.network.UserApi
import com.ronaker.app.data.network.request.UserRegisterRequestModel
import com.ronaker.app.data.network.response.UserInfoResponceModel
import com.ronaker.app.data.network.response.UserRegisterResponseModel
import com.ronaker.app.model.User
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit

class UserRepositoryTest {

    @Rule
    @JvmField
    val rule = MockitoJUnit.rule()!!

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    lateinit var userApi: UserApi

    @Mock
    lateinit var preferences: PreferencesDataSource

    // Class under test
    private lateinit var userRepository: UserRepository


    @Before
    fun createRepository() {
        preferences = FakePreferencesProvider()

        userRepository = DefaultUserRepository(userApi, preferences)
    }


    @Test
    fun registerUser_newUser() {


        var user = User(
            null,
            "test@test.com",
            null,
            "testName",
            "testLast",
            null,
            null,
            null,
            null,
            null,
            "testPass"
        )

        var request =
            UserRegisterRequestModel(user.email, user.password, user.first_name, user.last_name,null)

        var responce = UserRegisterResponseModel(
            "gfgfg",
            UserInfoResponceModel(
                "ddsf",
                user.email,
                false,
                user.first_name,
                user.last_name,
                null,
                false,
                null,
                false,
                false,
                0.0
            )
        )

        Mockito.`when`(userApi.registerUser(request)).thenReturn(Observable.just(responce))

        var result = userRepository.registerUser(user)


        val testObserver = TestObserver<Result<User>>()
        result.subscribe(testObserver)
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        val listResult = testObserver.values()[0]
        assertThat(listResult.data?.accessToken, `is`("gfgfg"))


    }



}