package com.fernandocejas.android10.sample.presentation.view.device.router.data;

import com.fernandocejas.android10.sample.presentation.R;

import java.util.ArrayList;
import java.util.List;


/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/01
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class Router {

  /**
   * 网关设置列表
   */
  private List<RouterSetting> mRouterSettings = new ArrayList<>();

  /**
   * 网关基本信息
   */
  private BaseInfo mBaseInfo;
  
  
  private List<VPN> mVPNs;


  public Router() {
    init();
  }

  private void init() {
    mRouterSettings.add(new RouterSetting(R.drawable.ic_infor, "网关信息", ""));
    mRouterSettings.add(new RouterSetting(R.drawable.ic_wifi, "WiFi设置", ""));
    mRouterSettings.add(new RouterSetting(R.drawable.ic_net, "上网方式", ""));
    mRouterSettings.add(new RouterSetting(R.drawable.ic_password, "管理密码", ""));
    mRouterSettings.add(new RouterSetting(R.drawable.ic_reboot, "网关定时重启", ""));
    mRouterSettings.add(new RouterSetting(R.drawable.ic_update, "固件升级", ""));
    mRouterSettings.add(new RouterSetting(R.drawable.vpn, "vpn设置", ""));
  }

  public List<RouterSetting> getRouterSettings() {
    return mRouterSettings;
  }

  public BaseInfo getBaseInfo() {
    return mBaseInfo;
  }

  public List<VPN> getVPNs() {
    return mVPNs;
  }

  public void setVPNs(List<VPN> VPNs) {
    mVPNs = VPNs;
  }

  public void setBaseInfo(BaseInfo baseInfo) {
    mBaseInfo = baseInfo;
  }

  public static class RouterSetting {

    private int iconId;
    private String name;
    private String status;

    public RouterSetting(int iconId, String name, String status) {
      this.iconId = iconId;
      this.name = name;
      this.status = status;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    public int getIconId() {
      return iconId;
    }

    public void setIconId(int iconId) {
      this.iconId = iconId;
    }
  }

  public static class BaseInfo {

    private String name;
    private String group;
    private String description;
    private String towPFourG;
    private String fiveG;
    private String lanIp;
    private String wanIp;
    private String wanMac;
    private String cpuModel;
    private String cpuBrand;
    private String cpuArch;
    private String cpuBasicFrequency;
    private String transFrequency;
    private String intenalMemory;
    private String flashMemory;

    public BaseInfo() {
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getGroup() {
      return group;
    }

    public void setGroup(String group) {
      this.group = group;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public String getTowPFourG() {
      return towPFourG;
    }

    public void setTowPFourG(String towPFourG) {
      this.towPFourG = towPFourG;
    }

    public String getFiveG() {
      return fiveG;
    }

    public void setFiveG(String fiveG) {
      this.fiveG = fiveG;
    }

    public String getLanIp() {
      return lanIp;
    }

    public void setLanIp(String lanIp) {
      this.lanIp = lanIp;
    }

    public String getWanIp() {
      return wanIp;
    }

    public void setWanIp(String wanIp) {
      this.wanIp = wanIp;
    }

    public String getCpuModel() {
      return cpuModel;
    }

    public void setCpuModel(String cpuModel) {
      this.cpuModel = cpuModel;
    }

    public String getCpuBrand() {
      return cpuBrand;
    }

    public void setCpuBrand(String cpuBrand) {
      this.cpuBrand = cpuBrand;
    }

    public String getCpuArch() {
      return cpuArch;
    }

    public void setCpuArch(String cpuArch) {
      this.cpuArch = cpuArch;
    }

    public String getWanMac() {
      return wanMac;
    }

    public void setWanMac(String wanMac) {
      this.wanMac = wanMac;
    }

    public String getCpuBasicFrequency() {
      return cpuBasicFrequency;
    }

    public void setCpuBasicFrequency(String cpuBasicFrequency) {
      this.cpuBasicFrequency = cpuBasicFrequency;
    }

    public String getTransFrequency() {
      return transFrequency;
    }

    public void setTransFrequency(String transFrequency) {
      this.transFrequency = transFrequency;
    }

    public String getIntenalMemory() {
      return intenalMemory;
    }

    public void setIntenalMemory(String intenalMemory) {
      this.intenalMemory = intenalMemory;
    }

    public String getFlashMemory() {
      return flashMemory;
    }

    public void setFlashMemory(String flashMemory) {
      this.flashMemory = flashMemory;
    }

    public List<InfoItem> getInfoItems() {
      List<InfoItem> infoItems = new ArrayList<>();
      infoItems.add(new InfoItem("2.4G WiFi名称", getTowPFourG()));
      infoItems.add(new InfoItem("5G WiFi名称", getFiveG(), true));
      infoItems.add(new InfoItem("LAN口IP地址", getLanIp()));
      infoItems.add(new InfoItem("WAN口IP地址", getWanIp()));
      infoItems.add(new InfoItem("WAN口MAC地址", getWanMac(), true));
      infoItems.add(new InfoItem("处理器型号", getCpuModel()));
      infoItems.add(new InfoItem("处理器品牌", getCpuBrand()));
      infoItems.add(new InfoItem("处理器架构", getCpuArch()));
      infoItems.add(new InfoItem("处理器主频", getCpuBasicFrequency(), true));
      infoItems.add(new InfoItem("无线传输主频", getTransFrequency()));
      infoItems.add(new InfoItem("内存", getIntenalMemory()));
      infoItems.add(new InfoItem("闪存", getFlashMemory()));
      return infoItems;

    }

    public static class InfoItem {

      private String title;
      private String name;
      private boolean hasFoot;

      public InfoItem() {
      }

      public InfoItem(String title, String name) {
        this(title, name, false);

      }

      public InfoItem(String title, String name, boolean hasFoot) {
        this.title = title;
        this.name = name;
        this.hasFoot = hasFoot;
      }

      public String getTitle() {
        return title;
      }

      public void setTitle(String title) {
        this.title = title;
      }

      public String getName() {
        return name;
      }

      public void setName(String name) {
        this.name = name;
      }

      public boolean isHasFoot() {
        return hasFoot;
      }

      public void setHasFoot(boolean hasFoot) {
        this.hasFoot = hasFoot;
      }

      @Override
      public String toString() {
        return "InfoItem{" +
            "title='" + title + '\'' +
            ", name='" + name + '\'' +
            ", hasFoot=" + hasFoot +
            '}';
      }
    }


  }

  public static class VPN {

    private String id;
    private String name;

    public VPN(String id, String name) {
      this.id = id;
      this.name = name;
    }

    public VPN(String name) {
      this("id_" + name, name);
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}
