package com.fernandocejas.android10.sample.presentation.view.mine.myinfo;

import android.content.Context;

import com.fernandocejas.android10.sample.domain.interactor.cloud.ModifyPwdGetIdCodeUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.ModifyPwdUseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.mapp.model.req.ModifyPwdGetIdCodeRequest;
import com.qtec.mapp.model.req.ModifyPwdRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Subscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ModifyPwdPresenterTest {


    @Mock
    private ModifyPwdView modifyPwdView;

    @Mock
    private ModifyPwdGetIdCodeUseCase modifyPwdGetIdCodeUseCase;

    @Mock
    private ModifyPwdUseCase modifyPwdUseCase;

    @Mock
    private Context mockContext;

    private ModifyPwdPresenter modifyPwdPresenter;


    @Before
    public void setupTasksPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        modifyPwdPresenter = new ModifyPwdPresenter(modifyPwdGetIdCodeUseCase, modifyPwdUseCase);

        modifyPwdPresenter.setView(modifyPwdView);

    }
//    @Test
//    public void getIdCode_showPhoneEmp() {
//        QtecEncryptInfo<ModifyPwdGetIdCodeRequest> encryptInfo = new QtecEncryptInfo<>();
//        ModifyPwdGetIdCodeRequest request = new ModifyPwdGetIdCodeRequest();
//        encryptInfo.setData(request);
//
//        when(modifyPwdView.getContext()).thenReturn(mockContext);
//        modifyPwdPresenter.getIdCode(encryptInfo);
//
//        verify(modifyPwdView).showUserPhoneEmp();
//        verify(modifyPwdView, never()).showLoading();
//        verify(modifyPwdGetIdCodeUseCase, never()).execute(any(IRequest.class), any(Subscriber.class));
//    }
//
//    @Test
//    public void getIdCode_success() {
//
//        QtecEncryptInfo<ModifyPwdGetIdCodeRequest> encryptInfo = new QtecEncryptInfo<>();
//        ModifyPwdGetIdCodeRequest request = new ModifyPwdGetIdCodeRequest();
//        request.setUserPhone("12345678");
//        encryptInfo.setData(request);
//
//        when(modifyPwdView.getContext()).thenReturn(mockContext);
//        modifyPwdPresenter.getIdCode(encryptInfo);
//
//        verify(modifyPwdView).showLoading();
//        verify(modifyPwdGetIdCodeUseCase).execute(any(IRequest.class), any(Subscriber.class));
//    }
//
//    @Test
//    public void modifyPwd_showUserPhoneEmpty() {
//        QtecEncryptInfo<ModifyPwdRequest> encryptInfo = new QtecEncryptInfo<>();
//        ModifyPwdRequest request = new ModifyPwdRequest();
//        encryptInfo.setData(request);
//
//        when(modifyPwdView.getContext()).thenReturn(mockContext);
//        modifyPwdPresenter.modifyPwd(encryptInfo);
//
//        verify(modifyPwdView).showUserPhoneEmp();
//        verify(modifyPwdView, never()).showLoading();
//        verify(modifyPwdUseCase, never()).execute(any(IRequest.class), any(Subscriber.class));
//    }
//
//
//    @Test
//    public void modifyPwd_showIdCodeEmpty() {
//        QtecEncryptInfo<ModifyPwdRequest> encryptInfo = new QtecEncryptInfo<>();
//        ModifyPwdRequest request = new ModifyPwdRequest();
//        request.setUserPhone("12345678");
//        encryptInfo.setData(request);
//
//        when(modifyPwdView.getContext()).thenReturn(mockContext);
//        modifyPwdPresenter.modifyPwd(encryptInfo);
//
//        verify(modifyPwdView).showIdCodeEmp();
//        verify(modifyPwdView, never()).showLoading();
//        verify(modifyPwdUseCase, never()).execute(any(IRequest.class), any(Subscriber.class));
//    }
//
//    @Test
//    public void modifyPwd_showPwdEmpty() {
//        QtecEncryptInfo<ModifyPwdRequest> encryptInfo = new QtecEncryptInfo<>();
//        ModifyPwdRequest request = new ModifyPwdRequest();
//        request.setUserPhone("123456");
//        request.setSmsCode("275673");
//        encryptInfo.setData(request);
//
//        when(modifyPwdView.getContext()).thenReturn(mockContext);
//        modifyPwdPresenter.modifyPwd(encryptInfo);
//
//        verify(modifyPwdView).showPasswordEmp();
//        verify(modifyPwdView, never()).showLoading();
//        verify(modifyPwdUseCase, never()).execute(any(IRequest.class), any(Subscriber.class));
//    }
//
//    @Test
//    public void modifyPwd_success() {
//        QtecEncryptInfo<ModifyPwdRequest> encryptInfo = new QtecEncryptInfo<>();
//        ModifyPwdRequest request = new ModifyPwdRequest();
//        request.setUserPhone("123456");
//        request.setSmsCode("275673");
//        request.setUserPassword("123456");
//        encryptInfo.setData(request);
//
//        when(modifyPwdView.getContext()).thenReturn(mockContext);
//        modifyPwdPresenter.modifyPwd(encryptInfo);
//
//        verify(modifyPwdView).showLoading();
//        verify(modifyPwdUseCase).execute(any(IRequest.class), any(Subscriber.class));
//    }

}