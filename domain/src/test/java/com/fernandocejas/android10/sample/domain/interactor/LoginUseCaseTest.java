package com.fernandocejas.android10.sample.domain.interactor;

import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.interactor.cloud.LoginUseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * @author shaojun
 * @name LoginUseCaseTest
 * @package com.fernandocejas.android10.sample.domain.interactor
 * @date 15-9-10
 */
public class LoginUseCaseTest {

  private static final String USERNAME = "user";
  private static final String PASSWORD = "123456";

  private LoginUseCase loginUseCase;

  @Mock private ThreadExecutor mockThreadExecutor;
  @Mock private PostExecutionThread mockPostExecutionThread;
  @Mock private CloudRepository mockCloudRepository;

  @Mock private IRequest mockParam;

  @Before public void setUp() throws Exception {

    MockitoAnnotations.initMocks(this);
    loginUseCase = new LoginUseCase(mockCloudRepository, mockThreadExecutor, mockPostExecutionThread);
  }

  @Test public void testBuildObservable() {
//    loginUseCase.buildUseCaseObservable(mockParam);
//
//    verify(mockCloudRepository).login(mockParam);
//    verifyNoMoreInteractions(mockCloudRepository);
//    verifyZeroInteractions(mockPostExecutionThread);
//    verifyZeroInteractions(mockThreadExecutor);
  }
}