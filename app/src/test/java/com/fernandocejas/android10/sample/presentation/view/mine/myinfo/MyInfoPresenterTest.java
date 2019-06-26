package com.fernandocejas.android10.sample.presentation.view.mine.myinfo;

import android.content.Context;

import com.fernandocejas.android10.sample.domain.interactor.cloud.LogoutUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.ModifyUserInfoUseCase;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class MyInfoPresenterTest {

    @Mock
    private MyInfoView myInfoView;

    @Mock
    private ModifyUserInfoUseCase modifyUserInfoUseCase;

    @Mock
    private LogoutUseCase logoutUseCase;

    @Mock
    private Context mockContext;

    private MyInfoPresenter myInfoPresenter;

//    @Before
//    public void setupTasksPresenter() {
//        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
//        // inject the mocks in the test the initMocks method needs to be called.
//        MockitoAnnotations.initMocks(this);
//
//        // Get a reference to the class under test
//        myInfoPresenter = new MyInfoPresenter(modifyUserInfoUseCase, logoutUseCase, getStsTokenUseCase);
//
//        myInfoPresenter.setView(myInfoView);
//
//    }

//    @Test
//    public void modify_showPhoneEmp() {
//        QtecEncryptInfo<ModifyUserInfoRequest> encryptInfo = new QtecEncryptInfo<>();
//        ModifyUserInfoRequest request = new ModifyUserInfoRequest();
//        encryptInfo.setData(request);
//
//        when(myInfoView.getContext()).thenReturn(mockContext);
//        myInfoPresenter.modifyUserInfo(encryptInfo);
//
//        verify(myInfoView).showUserPhoneEmp();
//        verify(myInfoView, never()).showLoading();
//        verify(modifyUserInfoUseCase, never()).execute(any(IRequest.class), any(Subscriber.class));
//    }
//
//    @Test
//    public void modify_showUserHeadEmp() {
//        QtecEncryptInfo<ModifyUserInfoRequest> encryptInfo = new QtecEncryptInfo<>();
//        ModifyUserInfoRequest request = new ModifyUserInfoRequest();
//        request.setUserPhone("1123424");
//        encryptInfo.setData(request);
//
//        when(myInfoView.getContext()).thenReturn(mockContext);
//        myInfoPresenter.modifyUserInfo(encryptInfo);
//
//        verify(myInfoView).showUserHeadImgEmp();
//        verify(myInfoView, never()).showUserPhoneEmp();
//        verify(myInfoView, never()).showLoading();
//        verify(modifyUserInfoUseCase, never()).execute(any(IRequest.class), any(Subscriber.class));
//    }
//
//    @Test
//    public void logout_success() {
//        QtecEncryptInfo<LogoutRequest> encryptInfo = new QtecEncryptInfo<>();
//        LogoutRequest request = new LogoutRequest();
//        request.setUserPhone("1123424");
//        encryptInfo.setData(request);
//
//        when(myInfoView.getContext()).thenReturn(mockContext);
//        myInfoPresenter.logout(encryptInfo);
//
//        verify(myInfoView).showLoading();
//        verify(logoutUseCase).execute(any(IRequest.class), any(Subscriber.class));
//    }

//    @Test
//    public void modify_success() {
//        QtecEncryptInfo<ModifyUserInfoRequest> encryptInfo = new QtecEncryptInfo<>();
//        ModifyUserInfoRequest request = new ModifyUserInfoRequest();
//        request.setUserPhone("1123424");
//        request.setUserHeadImg("kdjfdg");
//        request.setUserNickName("nick");
//        encryptInfo.setData(request);
//
//        when(myInfoView.getContext()).thenReturn(mockContext);
//        myInfoPresenter.modifyUserInfo(encryptInfo);
//
//        verify(myInfoView).showLoading();
//        verify(modifyUserInfoUseCase).execute(any(IRequest.class), any(Subscriber.class));
//    }
}