package com.qtec.mapp.model.rsp;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GetStsTokenResponse<T1, T2> {
  /**
   * requestId : BD18BFBA-88EB-48A2-8603-6CCE35E8A2B8
   * credentials : {"securityToken":"CAIS9gJ1q6Ft5B2yfSjIqpncBuKHp6Zm1YyuMVaFhUYNZctO2Z/vpjz2IHFJdHlpBu8et/owmWBR5vwZlqB0UIQAWUvHYM0oMCTcYL/iMeT7oMWQweEuLvTHcDHhq3eZsebWZ+LmNvO/Ht6md1HDkAJq3LL+bk/Mdle5MJqP+8YFC9MMRVuAcCZhDtVbLRcAzcgBLin+OOqKOBzniXaydE1zoVhQhGdy9amYyM+R4Qakrz+c8OIOoJnrKZWJdtRlOIwFM+24x+trbKHMomkyrhFB7/Vxl7cWu2We4oHNWQgPvU7dbrWFrY02fhZla6N/AalZsPW5lfBxtu3WmP63qXdsJchZXz7SX5vau8LPA7GSO90kb7vHIX3LyciILIWH3WFIfWgGOSEyIoZxeiUhV0J3EWyFe/D3oWqnOFnzF/K3t4gtyodwwlnS+t6HGkOCWb3x01xDa8JsPxp3aEZPgzO8IvZWKVdWElR8HPOJSoZ+agCzM2MBRNZoPBqAASbY1zBwhFVRZ/X/bveRZ420cAwhQs2ND9CPighy/QprAHmuw77niYMDZlCVCk7CxOdI4GwEjpIsX1CUedlCSGV0IzaaBy4vum7BCiH0lZ/vPjTE+Tzur+u3YNQqdXjVZHRVDD8e6PFGJi1BJNTmoTGBBAn03XnZOY8MmB8W/qwz","accessKeySecret":"5rsThNtbDKYtFYNbghYPevza9GcHP1CYxCyVyKobTtSp","accessKeyId":"STS.KZiMX3JyGbNE3p4aFXiDb6ZDD","expiration":"2017-07-17T06:25:16Z"}
   * assumedRoleUser : {"arn":"acs:ram::1488296633579781:role/oss-token/oss-token","assumedRoleId":"395881770167581823:oss-token"}
   */

  private String requestId;
  private T1 credentials;
  private T2 assumedRoleUser;

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public T1 getCredentials() {
    return credentials;
  }

  public void setCredentials(T1 credentials) {
    this.credentials = credentials;
  }


  public T2 getAssumedRoleUser() {
    return assumedRoleUser;
  }

  public void setAssumedRoleUser(T2 assumedRoleUser) {
    this.assumedRoleUser = assumedRoleUser;
  }

  public static class CredentialsBean {
    /**
     * securityToken : CAIS9gJ1q6Ft5B2yfSjIqpncBuKHp6Zm1YyuMVaFhUYNZctO2Z/vpjz2IHFJdHlpBu8et/owmWBR5vwZlqB0UIQAWUvHYM0oMCTcYL/iMeT7oMWQweEuLvTHcDHhq3eZsebWZ+LmNvO/Ht6md1HDkAJq3LL+bk/Mdle5MJqP+8YFC9MMRVuAcCZhDtVbLRcAzcgBLin+OOqKOBzniXaydE1zoVhQhGdy9amYyM+R4Qakrz+c8OIOoJnrKZWJdtRlOIwFM+24x+trbKHMomkyrhFB7/Vxl7cWu2We4oHNWQgPvU7dbrWFrY02fhZla6N/AalZsPW5lfBxtu3WmP63qXdsJchZXz7SX5vau8LPA7GSO90kb7vHIX3LyciILIWH3WFIfWgGOSEyIoZxeiUhV0J3EWyFe/D3oWqnOFnzF/K3t4gtyodwwlnS+t6HGkOCWb3x01xDa8JsPxp3aEZPgzO8IvZWKVdWElR8HPOJSoZ+agCzM2MBRNZoPBqAASbY1zBwhFVRZ/X/bveRZ420cAwhQs2ND9CPighy/QprAHmuw77niYMDZlCVCk7CxOdI4GwEjpIsX1CUedlCSGV0IzaaBy4vum7BCiH0lZ/vPjTE+Tzur+u3YNQqdXjVZHRVDD8e6PFGJi1BJNTmoTGBBAn03XnZOY8MmB8W/qwz
     * accessKeySecret : 5rsThNtbDKYtFYNbghYPevza9GcHP1CYxCyVyKobTtSp
     * accessKeyId : STS.KZiMX3JyGbNE3p4aFXiDb6ZDD
     * expiration : 2017-07-17T06:25:16Z
     */
    private String securityToken;
    private String accessKeySecret;
    private String accessKeyId;
    private String expiration;

    public String getSecurityToken() {
      return securityToken;
    }

    public void setSecurityToken(String securityToken) {
      this.securityToken = securityToken;
    }

    public String getAccessKeySecret() {
      return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
      this.accessKeySecret = accessKeySecret;
    }

    public String getAccessKeyId() {
      return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
      this.accessKeyId = accessKeyId;
    }

    public String getExpiration() {
      return expiration;
    }

    public void setExpiration(String expiration) {
      this.expiration = expiration;
    }
  }

  public static class AssumedRoleUserBean {
    /**
     * arn : acs:ram::1488296633579781:role/oss-token/oss-token
     * assumedRoleId : 395881770167581823:oss-token
     */
    private String arn;
    private String assumedRoleId;

    public String getArn() {
      return arn;
    }

    public void setArn(String arn) {
      this.arn = arn;
    }

    public String getAssumedRoleId() {
      return assumedRoleId;
    }

    public void setAssumedRoleId(String assumedRoleId) {
      this.assumedRoleId = assumedRoleId;
    }
  }


}
