package com.fernandocejas.android10.sample.data.net;

import com.fernandocejas.android10.sample.data.ApplicationTestCase;
import com.fernandocejas.android10.sample.domain.mapper.JsonMapper;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.qtec.mapp.model.rsp.LoginResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;

import rx.Observable;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author shaojun
 * @name CloudRestApiImplTest
 * @package com.fernandocejas.android10.sample.data.net
 * @date 15-9-11
 */
public class CloudRestApiImplTest extends ApplicationTestCase {

  public static final String USER = "user";
  public static final String PASSWORD = "123456";
  private CloudRestApi cloudRestApi;

  @Mock private JsonMapper mockJsonMapper;
  @Mock private IRequest mockParam;

  @Before public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    cloudRestApi = new CloudRestApiImpl(RuntimeEnvironment.application);
  }

  @Test public void testYZ0001Login() throws Exception {
//    Observable<LoginResponse> observable = cloudRestApi.login(mockParam);
//
//    assertThat(observable, is(notNullValue()));
//    assertThat(observable, is(instanceOf(Observable.class)));
  }
}