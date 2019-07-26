package com.xq.LegouShop.bean;

import java.io.Serializable;

//实名认证
public class AddressBean implements Serializable {
    public String id;//	收货地址id
    public String contacts;//	联系人
    public String contactsPhoneNumber;//		联系人电话
    public String provinceId;//		省id
    public String cityId;//	市id
    public String areaId;//	县id
    public String address;//	详细地址
    public int isDefault;//	是否是默认收获地址，0否1是

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContactsPhoneNumber() {
        return contactsPhoneNumber;
    }

    public void setContactsPhoneNumber(String contactsPhoneNumber) {
        this.contactsPhoneNumber = contactsPhoneNumber;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return "AddressBean{" +
                "id='" + id + '\'' +
                ", contacts='" + contacts + '\'' +
                ", contactsPhoneNumber='" + contactsPhoneNumber + '\'' +
                ", provinceId='" + provinceId + '\'' +
                ", cityId='" + cityId + '\'' +
                ", areaId='" + areaId + '\'' +
                ", address='" + address + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }
}
