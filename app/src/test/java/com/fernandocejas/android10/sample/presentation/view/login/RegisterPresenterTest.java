package com.fernandocejas.android10.sample.presentation.view.login;

import android.content.Context;

import com.fernandocejas.android10.sample.domain.interactor.cloud.GetIdCodeUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.RegisterUseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.presentation.view.login.register.RegisterPresenter;
import com.fernandocejas.android10.sample.presentation.view.login.register.RegisterView;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.mapp.model.req.GetIdCodeRequest;
import com.qtec.mapp.model.req.RegisterRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Subscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class RegisterPresenterTest {

    @Mock
    private RegisterView registerView;

    @Mock
    private GetIdCodeUseCase getIdCodeUseCase;

    @Mock
    private RegisterUseCase registerUseCase;

    @Mock
    private Context mockContext;

    private RegisterPresenter registerPresenter;

    private QtecEncryptInfo<GetIdCodeRequest> getIdCodeEncryptInfo;

    @Before
    public void setupTasksPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Make the sure that all schedulers are immediate.
//        mSchedulerProvider = new ImmediateSchedulerProvider();

        // Get a reference to the class under test
        registerPresenter = new RegisterPresenter(getIdCodeUseCase, registerUseCase);

        registerPresenter.setView(registerView);
        getIdCodeEncryptInfo = new QtecEncryptInfo<>();
        GetIdCodeRequest getIdCodeRequest = new GetIdCodeRequest();
        this.getIdCodeEncryptInfo.setData(getIdCodeRequest);
    }
//
//    @Test
//    public void enterEmptyNumber_showEnterEmptyNumber() {
//        String userPhone = "";
//        getIdCodeEncryptInfo.getData().setUserPhone(userPhone);
//        when(registerView.getContext()).thenReturn(mockContext);
//        registerPresenter.getIdCode(userPhone);
//
//        verify(registerView).showUserPhoneEmp();
//        verify(registerView, never()).showLoading();
//        verify(getIdCodeUseCase, never()).execute(any(IRequest.class), any(Subscriber.class));
//    }
//
//    @Test
//    public void getIdCode_success() {
//
//        getIdCodeEncryptInfo.getData().setUserPhone("13812341234");
//
//        when(registerView.getContext()).thenReturn(mockContext);
//        registerPresenter.getIdCode("sdfsdf");
//
//        verify(registerView).showLoading();
//        verify(getIdCodeUseCase).execute(any(IRequest.class), any(Subscriber.class));
//    }
//
//    @Test
//    public void register_id_code_empty() {
//        QtecEncryptInfo<RegisterRequest> encryptInfo = new QtecEncryptInfo<>();
//        RegisterRequest request = new RegisterRequest();
//        request.setUserPhone("12345678");
//        encryptInfo.setData(request);
//
//        when(registerView.getContext()).thenReturn(mockContext);
//        registerPresenter.register("");
//
//        verify(registerView).showIdCodeEmp();
//        verify(registerView, never()).showLoading();
//        verify(registerUseCase, never()).execute(any(IRequest.class), any(Subscriber.class));
//    }
//
//    @Test
//    public void register_userPhone_empty() {
//        QtecEncryptInfo<RegisterRequest> encryptInfo = new QtecEncryptInfo<>();
//        RegisterRequest request = new RegisterRequest();
//        encryptInfo.setData(request);
//
//        when(registerView.getContext()).thenReturn(mockContext);
//        registerPresenter.register(encryptInfo);
//
//        verify(registerView).showUserPhoneEmp();
//        verify(registerView, never()).showLoading();
//        verify(registerUseCase, never()).execute(any(IRequest.class), any(Subscriber.class));
//    }
//
//    @Test
//    public void register_psw_empty() {
//        QtecEncryptInfo<RegisterRequest> encryptInfo = new QtecEncryptInfo<>();
//        RegisterRequest request = new RegisterRequest();
//        request.setUserPhone("123456");
//        request.setSmsCode("275673");
//        encryptInfo.setData(request);
//
//        when(registerView.getContext()).thenReturn(mockContext);
//        registerPresenter.register(encryptInfo);
//
//        verify(registerView).showPasswordEmp();
//        verify(registerView, never()).showLoading();
//        verify(registerUseCase, never()).execute(any(IRequest.class), any(Subscriber.class));
//    }
//
//    @Test
//    public void register_success() {
//        QtecEncryptInfo<RegisterRequest> encryptInfo = new QtecEncryptInfo<>();
//        RegisterRequest request = new RegisterRequest();
//        request.setUserPhone("123456");
//        request.setSmsCode("275673");
//        request.setUserPassword("123456");
//        encryptInfo.setData(request);
//
//        when(registerView.getContext()).thenReturn(mockContext);
//        registerPresenter.register(encryptInfo);
//
//        verify(registerView).showLoading();
//        verify(registerUseCase).execute(any(IRequest.class), any(Subscriber.class));
//    }

}