package com.fernandocejas.android10.sample.presentation.view.login;

import android.content.Context;

import com.fernandocejas.android10.sample.domain.interactor.cloud.ResetPwdGetIdCodeUseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.presentation.view.login.forgetpwd.ResetPwdGetIdCodePresenter;
import com.fernandocejas.android10.sample.presentation.view.login.forgetpwd.ResetPwdGetIdCodeView;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.mapp.model.req.ResetPwdGetIdCodeRequest;

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
public class ResetPwdGetIdCodePresenterTest {
    @Mock
    private ResetPwdGetIdCodeView resetPwdGetIdCodeView;

    @Mock
    private ResetPwdGetIdCodeUseCase resetPwdGetIdCodeUseCase;

    @Mock
    private Context mockContext;

    private ResetPwdGetIdCodePresenter resetPwdGetIdCodePresenter;

    @Before
    public void setupTasksPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        resetPwdGetIdCodePresenter = new ResetPwdGetIdCodePresenter(resetPwdGetIdCodeUseCase);

        resetPwdGetIdCodePresenter.setView(resetPwdGetIdCodeView);
    }

    @Test
    public void getIdCode_showPhoneEmp() {
        QtecEncryptInfo<ResetPwdGetIdCodeRequest> encryptInfo = new QtecEncryptInfo<>();
        ResetPwdGetIdCodeRequest request = new ResetPwdGetIdCodeRequest();
        encryptInfo.setData(request);

        when(resetPwdGetIdCodeView.getContext()).thenReturn(mockContext);
        resetPwdGetIdCodePresenter.getIdCode("");

        verify(resetPwdGetIdCodeView).showUserPhoneEmp();
        verify(resetPwdGetIdCodeView, never()).showLoading();
        verify(resetPwdGetIdCodeUseCase, never()).execute(any(IRequest.class), any(Subscriber.class));
    }

    @Test
    public void getIdCode_success() {

        QtecEncryptInfo<ResetPwdGetIdCodeRequest> encryptInfo = new QtecEncryptInfo<>();
        ResetPwdGetIdCodeRequest request = new ResetPwdGetIdCodeRequest();
        request.setUserPhone("12345678");
        encryptInfo.setData(request);

        when(resetPwdGetIdCodeView.getContext()).thenReturn(mockContext);
        resetPwdGetIdCodePresenter.getIdCode("sdfdf");

        verify(resetPwdGetIdCodeView).showLoading();
        verify(resetPwdGetIdCodeUseCase).execute(any(IRequest.class), any(Subscriber.class));
    }

}