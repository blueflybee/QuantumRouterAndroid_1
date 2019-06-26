package com.fernandocejas.android10.sample.presentation.view.device.fragment;

import android.content.Context;

import com.fernandocejas.android10.sample.domain.interactor.cloud.GetDevTreeUseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.qtec.mapp.model.req.GetDevTreeRequest;
import com.qtec.model.core.QtecEncryptInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Subscriber;

import static org.mockito.Matchers.any;
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
public class QDevPresenterTest {
//    @Mock
//    private QDevView qDevView;
//
//    @Mock
//    private GetDevTreeUseCase getDevTreeUseCase;
//
//    @Mock
//    private Context mockContext;
//
//    private QDevPresenter qDevPresenter;
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
//        qDevPresenter = new QDevPresenter(getDevTreeUseCase);
//
//        qDevPresenter.setView(qDevView);
//
//    }
//
//    @Test
//    public void getDevTree_showSuccess() {
//        GetDevTreeRequest data = new GetDevTreeRequest();
//        QtecEncryptInfo<GetDevTreeRequest> request = new QtecEncryptInfo<>();
//        request.setData(data);
//        when(qDevView.getContext()).thenReturn(mockContext);
//        qDevPresenter.getDevTree("");
//
//        verify(qDevView).showLoading();
//        verify(getDevTreeUseCase).execute(any(IRequest.class), any(Subscriber.class));
//    }

}