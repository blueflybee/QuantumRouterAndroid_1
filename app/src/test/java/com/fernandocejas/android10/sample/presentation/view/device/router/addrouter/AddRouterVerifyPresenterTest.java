package com.fernandocejas.android10.sample.presentation.view.device.router.addrouter;

import android.content.Context;

import com.fernandocejas.android10.sample.domain.interactor.cloud.CommitAddRouterInfoUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.AddRouterVerifyUseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.qtec.mapp.model.req.CommitAddRouterInfoRequest;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.AddRouterVerifyRequest;

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
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class AddRouterVerifyPresenterTest {
    @Mock
    private AddRouterVerifyView addRouterVerifyView;

    @Mock
    private AddRouterVerifyUseCase addRouterVerifyUseCase;

    @Mock
    private CommitAddRouterInfoUseCase commitAddRouterInfoUseCase;

    @Mock
    private Context mockContext;

    private AddRouterVerifyPresenter addRouterVerifyPresenter;

    @Before
    public void setupTasksPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Make the sure that all schedulers are immediate.
//        mSchedulerProvider = new ImmediateSchedulerProvider();

        // Get a reference to the class under test
        addRouterVerifyPresenter = new AddRouterVerifyPresenter(addRouterVerifyUseCase, commitAddRouterInfoUseCase);

        addRouterVerifyPresenter.setView(addRouterVerifyView);

    }
//
//    @Test
//    public void addRouterVerify_showPasswordEmp() {
//        QtecMultiEncryptInfo request = new QtecMultiEncryptInfo();
//
//        when(addRouterVerifyView.getContext()).thenReturn(mockContext);
//        addRouterVerifyPresenter.addRouterVerify(request, null);
//
//        verify(addRouterVerifyView).showAdminPwdEmp();
//        verify(addRouterVerifyView, never()).showLoading();
//        verify(addRouterVerifyUseCase, never()).execute(any(IRequest.class), any(Subscriber.class));
//    }

//    @Test
//    public void addRouterVerify_showPasswordEmp() {
//        QtecMultiEncryptInfo request = new QtecMultiEncryptInfo();
//
//        when(addRouterVerifyView.getContext()).thenReturn(mockContext);
//        addRouterVerifyPresenter.addRouterVerify(request, null);
//
//        verify(addRouterVerifyView).showAdminPwdEmp();
//        verify(addRouterVerifyView, never()).showLoading();
//        verify(addRouterVerifyUseCase, never()).execute(any(IRequest.class), any(Subscriber.class));
//    }
//
//    @Test
//    public void addRouterVerify_showSuccess() {
//        QtecMultiEncryptInfo request = new QtecMultiEncryptInfo();
//        when(addRouterVerifyView.getContext()).thenReturn(mockContext);
//
//        addRouterVerifyPresenter.addRouterVerify(request, "123456");
//        verify(addRouterVerifyView).showLoading();
//        verify(addRouterVerifyUseCase).execute(any(IRequest.class), any(Subscriber.class));
//    }
//
//    @Test
//    public void commitToCloud_showSuccess() {
//        QtecEncryptInfo<CommitAddRouterInfoRequest> request = new QtecEncryptInfo<>();
//        CommitAddRouterInfoRequest data = new CommitAddRouterInfoRequest();
//        request.setData(data);
//        when(addRouterVerifyView.getContext()).thenReturn(mockContext);
//        addRouterVerifyPresenter.commitRouterInfo(request);
//        verify(addRouterVerifyView).showLoading();
//        verify(commitAddRouterInfoUseCase).execute(any(IRequest.class), any(Subscriber.class));
//    }

}