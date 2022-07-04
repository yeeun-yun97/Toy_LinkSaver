package com.github.yeeun_yun97.toy.linksaver.test.viewModel.androidViewModels

//@HiltAndroidTest
//@RunWith(AndroidJUnit4::class)
//class SettingViewModelTest : SjBaseTest() {

//    @get:Rule
//    var activityRule: ActivityScenarioRule<MainActivity> =
//        ActivityScenarioRule(MainActivity::class.java)
//
//    @Inject // inject not working here!! T
//    private lateinit var viewModel: SettingViewModel
//
//    override fun before() {
//        viewModel
//    }
//
//    @Test
//    fun setPrivateModeOn(){
//        runBlocking(Dispatchers.Main) {
//            insertBaseData().join()
//
//            val isPrivateMode = true
//            viewModel.setPrivateMode(isPrivateMode).join()
//
//            val result = getValueOrThrow(viewModel.isPrivateMode)
//            Assert.assertEquals(isPrivateMode,result)
//        }
//    }
//
//    @Test
//    fun setPrivateModeOff(){
//        runBlocking(Dispatchers.Main) {
//            insertBaseData().join()
//
//            val isPrivateMode = false
//            viewModel.setPrivateMode(isPrivateMode).join()
//
//            val result = getValueOrThrow(viewModel.isPrivateMode)
//            Assert.assertEquals(isPrivateMode,result)
//        }
//    }
//
//    @Test
//    fun checkPasswordNotSet(){
//        runBlocking(Dispatchers.Main) {
//            insertBaseData().join()
//
//            val password = viewModel.passwordFlow.first()
//            Assert.assertEquals(true,password.isEmpty())
//        }
//    }

//}