package com.fernandocejas.android10.sample.presentation.view.login;

import android.content.Context;

import com.fernandocejas.android10.sample.domain.interactor.cloud.ResetPwdGetIdCodeUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.ResetPwdUseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.presentation.view.login.forgetpwd.ResetPwdPresenter;
import com.fernandocejas.android10.sample.presentation.view.login.forgetpwd.ResetPwdView;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.mapp.model.req.ResetPwdGetIdCodeRequest;
import com.qtec.mapp.model.req.ResetPwdRequest;

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
 *     time   : 2017/06/13
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ResetPwdPresenterTest {


    @Mock
    private ResetPwdView resetPwdView;

    @Mock
    private ResetPwdGetIdCodeUseCase resetPwdGetIdCodeUseCase;

    @Mock
    private ResetPwdUseCase resetPwdUseCase;

    @Mock
    private Context mockContext;

    private ResetPwdPresenter resetPwdPresenter;


    @Before
    public void setupTasksPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        resetPwdPresenter = new ResetPwdPresenter(resetPwdGetIdCodeUseCase, resetPwdUseCase);

        resetPwdPresenter.setView(resetPwdView);

    }


}