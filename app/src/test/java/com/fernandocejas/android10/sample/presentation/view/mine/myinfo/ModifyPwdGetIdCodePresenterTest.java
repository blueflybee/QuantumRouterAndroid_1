package com.fernandocejas.android10.sample.presentation.view.mine.myinfo;

import android.content.Context;

import com.fernandocejas.android10.sample.domain.interactor.cloud.ModifyPwdGetIdCodeUseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.mapp.model.req.ModifyPwdGetIdCodeRequest;

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
public class ModifyPwdGetIdCodePresenterTest {
    @Mock
    private ModifyPwdGetIdCodeView modifyPwdGetIdCodeView;

    @Mock
    private ModifyPwdGetIdCodeUseCase modifyPwdGetIdCodeUseCase;

    @Mock
    private Context mockContext;

    private ModifyPwdGetIdCodePresenter modifyPwdGetIdCodePresenter;

    @Before
    public void setupTasksPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        modifyPwdGetIdCodePresenter = new ModifyPwdGetIdCodePresenter(modifyPwdGetIdCodeUseCase);

        modifyPwdGetIdCodePresenter.setView(modifyPwdGetIdCodeView);
    }
//
//    @Test
//    public void getIdCode_showPhoneEmp() {
//        QtecEncryptInfo<ModifyPwdGetIdCodeRequest> encryptInfo = new QtecEncryptInfo<>();
//        ModifyPwdGetIdCodeRequest request = new ModifyPwdGetIdCodeRequest();
//        encryptInfo.setData(request);
//
//        when(modifyPwdGetIdCodeView.getContext()).thenReturn(mockContext);
//        modifyPwdGetIdCodePresenter.getIdCode(encryptInfo);
//
//        verify(modifyPwdGetIdCodeView).showUserPhoneEmp();
//        verify(modifyPwdGetIdCodeView, never()).showLoading();
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
//        when(modifyPwdGetIdCodeView.getContext()).thenReturn(mockContext);
//        modifyPwdGetIdCodePresenter.getIdCode(encryptInfo);
//
//        verify(modifyPwdGetIdCodeView).showLoading();
//        verify(modifyPwdGetIdCodeUseCase).execute(any(IRequest.class), any(Subscriber.class));
//    }

}