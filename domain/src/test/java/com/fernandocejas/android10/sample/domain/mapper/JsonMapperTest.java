package com.fernandocejas.android10.sample.domain.mapper;

import com.fernandocejas.android10.sample.domain.utils.AES;
import com.fernandocejas.android10.sample.domain.utils.QEncodeUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qtec.mapp.model.req.TransmitRequest;
import com.qtec.mapp.model.rsp.TransmitResponse;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecResult;
import com.qtec.mapp.model.req.LoginRequest;
import com.qtec.mapp.model.rsp.LoginResponse;
import com.qtec.router.model.req.RouterStatusRequest;
import com.qtec.router.model.rsp.AddIntelDevVerifyResponse;
import com.qtec.router.model.rsp.RouterStatusResponse;
import com.qtec.router.model.rsp.RouterStatusResponse.Status;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author shaojun
 * @name JsonMapperTest
 * @package com.fernandocejas.android10.sample.data.entity.mapper
 * @date 15-9-15
 */
public class JsonMapperTest {

//  private static final String EXPECTED_JSON =
//      "{\"@class\":\"com.bdx.mapp.model.BDXDatapackage\",\"header\":{\"@class\":\"com.bdx.mapp.model.BDXHeader\",\"bizCode\":\"yz0001\",\"identityId\":null,\"coordinates\":null,\"respCode\":null,\"respMsg\":null,\"mode\":null,\"sign\":null},\"body\":{\"@class\":\"com.bdx.mapp.model.req.YZ0001Request\",\"usercode\":\"username\",\"pwd\":\"123456\",\"sysType\":null,\"version\":\"1\",\"os\":\"android_4.4.2\",\"deviceType\":\"aPhone\",\"logonSys\":null}}";

  private static final String EXPECTED_JSON = "{\"method\":\"post\",\"data\":{\"userPhone\":\"13557321206\",\"userPassword\":\"dddd\"},\"requestUrl\":\"/route/login/pwdlogin\"}";
  private static final String EXPECTED_JSON_RSP = "{\"code\":0,\"msg\":\"ok\",\"data\":{\"userHeadImg\":\"img\",\"userNickName\":\"dddd\",\"userPhone\":\"13557321206\",\"token\":\"token\",\"userUniqueKey\":\"unkey\"}}";
  private static final String EXPECTED_JSON_RSP_LIST = "{\"code\":0,\"msg\":\"ok\",\"data\":[{\"userHeadImg\":\"img\",\"userNickName\":\"dddd\",\"userPhone\":\"13557321206\",\"token\":\"token\",\"userUniqueKey\":\"unkey\"},{\"userHeadImg\":\"img\",\"userNickName\":\"dddd\",\"userPhone\":\"13557321206\",\"token\":\"token\",\"userUniqueKey\":\"unkey\"},{\"userHeadImg\":\"img\",\"userNickName\":\"dddd\",\"userPhone\":\"13557321206\",\"token\":\"token\",\"userUniqueKey\":\"unkey\"},{\"userHeadImg\":\"img\",\"userNickName\":\"dddd\",\"userPhone\":\"13557321206\",\"token\":\"token\",\"userUniqueKey\":\"unkey\"}]}";

  private static final String EXPECTED_JSON_RSP_FEED_BACK_DETAIL = "{\"code\":0,\"msg\":null,\"data\":{\"feedbackContent\":\"新增\",\"replyContent\":[{\"time\":\"2018-05-01 22:01:02\",\"type\":\"0\",\"content\":\"已经收到反馈,请在我的反馈中查看\"},{\"time\":\"2018-05-01 22:01:02\",\"type\":\"1\",\"content\":\"更新一下\"},{\"time\":\"2018-05-01 22:01:02\",\"type\":\"0\",\"content\":\"已经收到反馈\"},{\"time\":\"2018-05-01 22:01:02\",\"type\":\"1\",\"content\":\"好的\"},{\"time\":\"2018-05-01 22:01:02\",\"type\":\"0\",\"content\":\"已经收到反馈\"}]}}";
  private static final String EXPECTED_JSON_RSP_TRANS = "{\"code\":0,\"msg\":null,\"data\":{\"encryptInfo\":\"{\\\"data\\\":{},\\\"msg\\\":\\\"ok\\\", \\\"code\\\":0}\"}}";


  private JsonMapper jsonMapper;

  @Before
  public void setUp() throws Exception {

    jsonMapper = new JsonMapper();
  }

  @After
  public void tearDown() throws Exception {

    jsonMapper = null;
  }

  @Test
  public void testToJson() {
    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setMethod("post");
    encryptInfo.setRequestUrl("/route/login/pwdlogin");

    LoginRequest request = new LoginRequest();
    request.setUserPhone("13557321206");
    request.setUserPassword("dddd");

    encryptInfo.setData(request);


    String actual = jsonMapper.toJson(encryptInfo);
    System.out.println("actual = " + actual);

    assertThat(actual, is(EXPECTED_JSON));
  }

  @Test
  public void testToJsonEmp() {
    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setMethod("post");
    encryptInfo.setRequestUrl("/route/login/pwdlogin");
    encryptInfo.setData(new EmpRequest());
    String actual = jsonMapper.toJson(encryptInfo);
    System.out.println("actual = " + actual);
  }


  @Test
  public void testToJson_otherMethod() {

//    QtecResult qtecResult = new QtecResult();
//    qtecResult.setCode(0);
//    qtecResult.setMsg("ok");
//    LoginResponse data = new LoginResponse();
//    data.setUserPhone("13557321206");
//    data.setUserNickName("dddd");
//    data.setToken("token");
//    data.setUserUniqueKey("unkey");
//    data.setUserHeadImg("img");
//    qtecResult.setData(data);
//
//    String actual = jsonMapper.toJson(qtecResult);
//    System.out.println("actual = " + actual);
//
//    assertThat(actual, is(EXPECTED_JSON_RSP));
  }

  @Test
  public void testToObject() {
    Type type = new TypeToken<QtecResult<LoginResponse>>() {
    }.getType();

    QtecResult<LoginResponse> actual = (QtecResult<LoginResponse>) jsonMapper.fromJson(EXPECTED_JSON_RSP, type);

    assertThat(actual, is(notNullValue()));

    QtecResult<LoginResponse> expected = new QtecResult<>();
    expected.setCode(0);
    expected.setMsg("ok");
    LoginResponse data = new LoginResponse();
    data.setUserPhone("13557321206");
    data.setUserNickName("dddd");
    data.setToken("token");
    data.setUserUniqueKey("unkey");
    data.setUserHeadImg("img");
    expected.setData(data);
    assertThat(actual, CoreMatchers.<QtecResult>is(expected));
    System.out.println("actual = " + actual);
    System.out.println("expected = " + expected);

    System.out.println("phone = " + expected.getData().getUserPhone());
    System.out.println("password = " + expected.getData().getUserNickName());

  }

  @Test
  public void testToObject_list() {
    Type type = new TypeToken<QtecResult<List<LoginResponse>>>() {
    }.getType();

    QtecResult actual = (QtecResult) jsonMapper.fromJson(EXPECTED_JSON_RSP_LIST, type);

    assertThat(actual, is(notNullValue()));

    QtecResult<List<LoginResponse>> expected = new QtecResult<>();
    List<LoginResponse> responseList = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      LoginResponse data = new LoginResponse();
      data.setUserPhone("13557321206");
      data.setUserNickName("dddd");
      data.setToken("token");
      data.setUserUniqueKey("unkey");
      data.setUserHeadImg("img");
      responseList.add(data);
    }
    expected.setData(responseList);
    expected.setMsg("ok");
    expected.setCode(0);
//        System.out.println("expected = " + jsonMapper.toJson(expected));

    assertThat(actual, CoreMatchers.<QtecResult>is(expected));
    System.out.println("actual = " + actual);
    System.out.println("expected = " + expected);

  }

  @Test
  public void testToObject_feedback_rsp() {
    Type type = new TypeToken<QtecResult<FeedBackRsp<List<FeedBackRsp.ReplyContent>>>>() {
    }.getType();

    QtecResult<FeedBackRsp<List<FeedBackRsp.ReplyContent>>> actual = (QtecResult) jsonMapper.fromJson(EXPECTED_JSON_RSP_FEED_BACK_DETAIL, type);

    assertThat(actual, is(notNullValue()));

    System.out.println("actual = " + actual);

    System.out.println("actual.getData().getReplyContent().get(0).getContent() = " + actual.getData().getReplyContent().get(0).getContent());

  }

  /**
   * app发送给网关的消息由云端转发
   */
  @Test
  public void testTransmitToJson() {
    QtecEncryptInfo<TransmitRequest> encryptInfo = new QtecEncryptInfo<>();
    encryptInfo.setMethod("post");
    encryptInfo.setRequestUrl("/route/route/dispatch");
    encryptInfo.setToken("askdjf90887jehfjd");

    TransmitRequest<QtecEncryptInfo<RouterStatusRequest>> request = new TransmitRequest<>();
    request.setRouterSerialNo("asdfasdfasdf");

    //封装app给网关的数据包
    QtecEncryptInfo<RouterStatusRequest> routerStatusEncryptInfo = new QtecEncryptInfo<>();
    routerStatusEncryptInfo.setRequestUrl("routerstatus");
    routerStatusEncryptInfo.setMethod("post");
    RouterStatusRequest routerStatusRequest = new RouterStatusRequest();
    routerStatusEncryptInfo.setData(routerStatusRequest);

    request.setEncryptInfo(routerStatusEncryptInfo);

    encryptInfo.setData(request);


    String actual = jsonMapper.toJson(encryptInfo);
    System.out.println("actual = " + actual);

//        assertThat(actual, is(EXPECTED_JSON));
  }

  @Test
  public void testTransmitToObject() throws Exception {

    //////////////////////////////封装网关给app的数据包
    QtecResult<RouterStatusResponse<List<Status>>> routerStatusEncryptInfo = new QtecResult<>();
    routerStatusEncryptInfo.setCode(0);
    routerStatusEncryptInfo.setMsg("router_msg");

    RouterStatusResponse<List<Status>> routerStatusResponse = new RouterStatusResponse<>();
    routerStatusResponse.setRouterrx(12);
    List<Status> statuses = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      Status status = new Status();
      status.setStaname("status" + i);
      status.setStastatus(1);
      statuses.add(status);
    }
    routerStatusResponse.setStalist(statuses);

    routerStatusEncryptInfo.setData(routerStatusResponse);

    String routerIncrypt = jsonMapper.toJson(routerStatusEncryptInfo);
    System.out.println("routerIncrypt = " + routerIncrypt);

    String aesEncrypt = AES.encrypt(routerIncrypt);
    System.out.println("aesEncrypt = " + aesEncrypt);


    ///////////////////////////////////
    TransmitResponse<String> response = new TransmitResponse<>();
    response.setEncryptInfo(aesEncrypt);


    QtecResult<TransmitResponse> encryptInfo = new QtecResult<>();
    encryptInfo.setMsg("cloud_msg");
    encryptInfo.setCode(0);
    encryptInfo.setData(response);


    String actual = jsonMapper.toJson(encryptInfo);
    System.out.println("actual = " + actual);

    Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
    }.getType();

    QtecResult<TransmitResponse<String>> actual_1 = (QtecResult<TransmitResponse<String>>) jsonMapper.fromJson(actual, type);

    System.out.println("actual = " + actual_1);
    String encryptInfoRsp = actual_1.getData().getEncryptInfo();
    System.out.println(encryptInfoRsp);


    String aesDecrypt = AES.decrypt(encryptInfoRsp);
    System.out.println("aesDecrypt = " + aesDecrypt);

    type = new TypeToken<QtecResult<RouterStatusResponse<List<Status>>>>() {
    }.getType();

    QtecResult<RouterStatusResponse<List<Status>>> result = (QtecResult<RouterStatusResponse<List<Status>>>) jsonMapper.fromJson(aesDecrypt, type);
    System.out.println("result = " + result);

  }

  @Test
  public void testToTransObject() {
    Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
    }.getType();

    QtecResult<TransmitResponse<String>> actual = (QtecResult<TransmitResponse<String>>) jsonMapper.fromJson(EXPECTED_JSON_RSP_TRANS, type);

    assertThat(actual, is(notNullValue()));

    System.out.println("actual = " + actual.getData().getEncryptInfo());


    type = new TypeToken<QtecResult<AddIntelDevVerifyResponse>>() {
    }.getType();
    QtecResult<AddIntelDevVerifyResponse> fromJson = (QtecResult<AddIntelDevVerifyResponse>) jsonMapper.fromJson(actual.getData().getEncryptInfo(), type);
    System.out.println("fromJson = " + fromJson);

    AddIntelDevVerifyResponse data = fromJson.getData();

  }

  class EmpRequest {

  }


}