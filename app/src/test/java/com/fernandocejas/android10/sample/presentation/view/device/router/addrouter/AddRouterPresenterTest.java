package com.fernandocejas.android10.sample.presentation.view.device.router.addrouter;

import android.content.Context;

import com.fernandocejas.android10.sample.domain.interactor.router.SearchRouterUseCase;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class AddRouterPresenterTest {
    @Mock
    private AddRouterView addRouterView;

    @Mock
    private SearchRouterUseCase searchRouterUseCase;

    @Mock
    private Context mockContext;

    private AddRouterPresenter addRouterPresenter;

    @Before
    public void setupTasksPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Make the sure that all schedulers are immediate.
//        mSchedulerProvider = new ImmediateSchedulerProvider();

        // Get a reference to the class under test
        addRouterPresenter = new AddRouterPresenter(searchRouterUseCase);

        addRouterPresenter.setView(addRouterView);

    }

//    @Test
//    public void searchRouter_showSuccess() {
//        QtecEncryptInfo<SearchRouterRequest> request = new QtecEncryptInfo<>();
//        when(addRouterView.getContext()).thenReturn(mockContext);
//        addRouterPresenter.searchRouter(request);
//        verify(addRouterView).showLoading();
//        verify(searchRouterUseCase).execute(any(IRequest.class), any(Subscriber.class));
//    }


}