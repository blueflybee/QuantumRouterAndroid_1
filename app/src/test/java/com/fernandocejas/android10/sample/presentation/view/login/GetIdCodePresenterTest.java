package com.fernandocejas.android10.sample.presentation.view.login;

import android.content.Context;

import com.fernandocejas.android10.sample.domain.interactor.cloud.GetIdCodeUseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.presentation.view.login.register.GetIdCodePresenter;
import com.fernandocejas.android10.sample.presentation.view.login.register.GetIdCodeView;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.mapp.model.req.GetIdCodeRequest;

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
public class GetIdCodePresenterTest {
    @Mock
    private GetIdCodeView getIdCodeView;

    @Mock
    private GetIdCodeUseCase getIdCodeUseCase;

    @Mock
    private Context mockContext;

    private GetIdCodePresenter getIdCodePresenter;

    private QtecEncryptInfo<GetIdCodeRequest> request;

    @Before
    public void setupTasksPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Make the sure that all schedulers are immediate.
//        mSchedulerProvider = new ImmediateSchedulerProvider();

        // Get a reference to the class under test
        getIdCodePresenter = new GetIdCodePresenter(getIdCodeUseCase);

        getIdCodePresenter.setView(getIdCodeView);
        request = new QtecEncryptInfo<>();
        GetIdCodeRequest getIdCodeRequest = new GetIdCodeRequest();
        request.setData(getIdCodeRequest);

        // The presenter won't update the view unless it's active.
//        when(loginView.isActive()).thenReturn(true);

        // We subscribe the tasks to 3, with one active and two completed
//        TASKS = Lists.newArrayList(new Task("Title1", "Description1"),
//                new Task("Title2", "Description2", true), new Task("Title3", "Description3", true));
    }

    @Test
    public void enterEmptyNumber_showEnterEmptyNumber() {
        String userPhone = "";
        request.getData().setUserPhone(userPhone);
        when(getIdCodeView.getContext()).thenReturn(mockContext);
        getIdCodePresenter.getIdCode(userPhone);

        verify(getIdCodeView).showUserPhoneEmp();
        verify(getIdCodeView, never()).showLoading();
        verify(getIdCodeUseCase, never()).execute(any(IRequest.class), any(Subscriber.class));
    }

    @Test
    public void getIdCode_success() {

        request.getData().setUserPhone("13812341234");

        when(getIdCodeView.getContext()).thenReturn(mockContext);
        getIdCodePresenter.getIdCode("13812341234");

        verify(getIdCodeView).showLoading();
        verify(getIdCodeUseCase).execute(any(IRequest.class), any(Subscriber.class));
//        verify(getIdCodeView).hideLoading();
    }

}