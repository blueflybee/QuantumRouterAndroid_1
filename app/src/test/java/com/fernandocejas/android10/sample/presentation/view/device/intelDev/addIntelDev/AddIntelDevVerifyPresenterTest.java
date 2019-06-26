package com.fernandocejas.android10.sample.presentation.view.device.intelDev.addIntelDev;

import android.content.Context;

import com.fernandocejas.android10.sample.domain.interactor.router.AddIntelDevVerifyUseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.qtec.model.core.QtecMultiEncryptInfo;

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
public class AddIntelDevVerifyPresenterTest {
//
//    @Mock
//    private AddIntelDevVerifyView addIntelDevVerifyView;
//
//    @Mock
//    private AddIntelDevVerifyUseCase addIntelDevVerifyUseCase;
//
//
//    @Mock
//    private Context mockContext;
//
//    private AddIntelDevVerifyPresenter addIntelDevVerifyPresenter;
//
//    @Before
//    public void setupTasksPresenter() {
//        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
//        // inject the mocks in the test the initMocks method needs to be called.
//        MockitoAnnotations.initMocks(this);
//
//        // Make the sure that all schedulers are immediate.
////        mSchedulerProvider = new ImmediateSchedulerProvider();
//
//        // Get a reference to the class under test
//        addIntelDevVerifyPresenter = new AddIntelDevVerifyPresenter(addIntelDevVerifyUseCase);
//
//        addIntelDevVerifyPresenter.setView(addIntelDevVerifyView);
//
//    }
//
//    @Test
//    public void addIntelDevVerify_showPasswordEmp() {
//        QtecMultiEncryptInfo request = new QtecMultiEncryptInfo();
//
//        when(addIntelDevVerifyView.getContext()).thenReturn(mockContext);
//        addIntelDevVerifyPresenter.addIntelDevVerify(request, "");
//
//        verify(addIntelDevVerifyView).showAdminPwdEmp();
//        verify(addIntelDevVerifyView, never()).showLoading();
//        verify(addIntelDevVerifyUseCase, never()).execute(any(IRequest.class), any(Subscriber.class));
//    }
//
//    @Test
//    public void addIntelDevVerify_showSuccess() {
//        QtecMultiEncryptInfo request = new QtecMultiEncryptInfo();
//        when(addIntelDevVerifyView.getContext()).thenReturn(mockContext);
//        addIntelDevVerifyPresenter.addIntelDevVerify(request, "123456");
//        verify(addIntelDevVerifyView).showLoading();
//        verify(addIntelDevVerifyUseCase).execute(any(IRequest.class), any(Subscriber.class));
//    }


}