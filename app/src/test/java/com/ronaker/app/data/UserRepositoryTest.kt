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
import io.reactivex.observers.TestObserver
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit

class UserRepositoryTest {

    @Rule
    @JvmField
    val rule = MockitoJUnit.rule()

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


        val user = User(
            suid = null,
          email =   "test@test.com",

           first_name =  "testName",
           last_name =  "testLast",
          password =   "testPass"
        )

        val request =
            UserRegisterRequestModel(user.email, user.password, user.first_name, user.last_name,null)

        val responce = UserRegisterResponseModel(
            "gfgfg",
            UserInfoResponceModel(
               suid =  "ddsf",
              email =   user.email,
              first_name =   user.first_name,
             last_name =    user.last_name,
                     is_email_verified =   false,
                is_identity_info_verified = false,
                is_payment_info_verified = false,
                is_phone_number_verified = false,
                smart_id_national_code = null,
                smart_id_personal_code = null,
                phone_number = null,
                avatar = null,
                balance = 0.0

            )
        )

        Mockito.`when`(userApi.registerUser(request)).thenReturn(Observable.just(responce))

        val result = userRepository.registerUser(user)


        val testObserver = TestObserver<Result<User>>()
        result.subscribe(testObserver)
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        val listResult = testObserver.values()[0]
        assertThat(listResult.data?.accessToken, `is`("gfgfg"))


    }



}