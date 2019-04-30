
package com.example.cryptomonitor.model_cryptocompare.model_coins;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class USD {

    @SerializedName("TYPE")
    @Expose
    private String tYPE;
    @SerializedName("MARKET")
    @Expose
    private String mARKET;
    @SerializedName("FROMSYMBOL")
    @Expose
    private String fROMSYMBOL;
    @SerializedName("TOSYMBOL")
    @Expose
    private String tOSYMBOL;
    @SerializedName("FLAGS")
    @Expose
    private String fLAGS;
    @SerializedName("PRICE")
    @Expose
    private Double pRICE;
    @SerializedName("LASTUPDATE")
    @Expose
    private Integer lASTUPDATE;
    @SerializedName("LASTVOLUME")
    @Expose
    private Double lASTVOLUME;
    @SerializedName("LASTVOLUMETO")
    @Expose
    private Double lASTVOLUMETO;
    @SerializedName("LASTTRADEID")
    @Expose
    private String lASTTRADEID;
    @SerializedName("VOLUMEDAY")
    @Expose
    private Double vOLUMEDAY;
    @SerializedName("VOLUMEDAYTO")
    @Expose
    private Double vOLUMEDAYTO;
    @SerializedName("VOLUME24HOUR")
    @Expose
    private Double vOLUME24HOUR;
    @SerializedName("VOLUME24HOURTO")
    @Expose
    private Double vOLUME24HOURTO;
    @SerializedName("OPENDAY")
    @Expose
    private Double oPENDAY;
    @SerializedName("HIGHDAY")
    @Expose
    private Double hIGHDAY;
    @SerializedName("LOWDAY")
    @Expose
    private Double lOWDAY;
    @SerializedName("OPEN24HOUR")
    @Expose
    private Double oPEN24HOUR;
    @SerializedName("HIGH24HOUR")
    @Expose
    private Double hIGH24HOUR;
    @SerializedName("LOW24HOUR")
    @Expose
    private Double lOW24HOUR;
    @SerializedName("LASTMARKET")
    @Expose
    private String lASTMARKET;
    @SerializedName("VOLUMEHOUR")
    @Expose
    private Double vOLUMEHOUR;
    @SerializedName("VOLUMEHOURTO")
    @Expose
    private Double vOLUMEHOURTO;
    @SerializedName("OPENHOUR")
    @Expose
    private Double oPENHOUR;
    @SerializedName("HIGHHOUR")
    @Expose
    private Double hIGHHOUR;
    @SerializedName("LOWHOUR")
    @Expose
    private Double lOWHOUR;
    @SerializedName("CHANGE24HOUR")
    @Expose
    private Double cHANGE24HOUR;
    @SerializedName("CHANGEPCT24HOUR")
    @Expose
    private Double cHANGEPCT24HOUR;
    @SerializedName("CHANGEDAY")
    @Expose
    private Double cHANGEDAY;
    @SerializedName("CHANGEPCTDAY")
    @Expose
    private Double cHANGEPCTDAY;
    @SerializedName("SUPPLY")
    @Expose
    private Double sUPPLY;
    @SerializedName("MKTCAP")
    @Expose
    private Double mKTCAP;
    @SerializedName("TOTALVOLUME24H")
    @Expose
    private Double tOTALVOLUME24H;
    @SerializedName("TOTALVOLUME24HTO")
    @Expose
    private Double tOTALVOLUME24HTO;
    @SerializedName("IMAGEURL")
    @Expose
    private String iMAGEURL;

    public String getTYPE() {
        return tYPE;
    }

    public void setTYPE(String tYPE) {
        this.tYPE = tYPE;
    }

    public String getMARKET() {
        return mARKET;
    }

    public void setMARKET(String mARKET) {
        this.mARKET = mARKET;
    }

    public String getFROMSYMBOL() {
        return fROMSYMBOL;
    }

    public void setFROMSYMBOL(String fROMSYMBOL) {
        this.fROMSYMBOL = fROMSYMBOL;
    }

    public String getTOSYMBOL() {
        return tOSYMBOL;
    }

    public void setTOSYMBOL(String tOSYMBOL) {
        this.tOSYMBOL = tOSYMBOL;
    }

    public String getFLAGS() {
        return fLAGS;
    }

    public void setFLAGS(String fLAGS) {
        this.fLAGS = fLAGS;
    }

    public Double getPRICE() {
        return pRICE;
    }

    public void setPRICE(Double pRICE) {
        this.pRICE = pRICE;
    }

    public Integer getLASTUPDATE() {
        return lASTUPDATE;
    }

    public void setLASTUPDATE(Integer lASTUPDATE) {
        this.lASTUPDATE = lASTUPDATE;
    }

    public Double getLASTVOLUME() {
        return lASTVOLUME;
    }

    public void setLASTVOLUME(Double lASTVOLUME) {
        this.lASTVOLUME = lASTVOLUME;
    }

    public Double getLASTVOLUMETO() {
        return lASTVOLUMETO;
    }

    public void setLASTVOLUMETO(Double lASTVOLUMETO) {
        this.lASTVOLUMETO = lASTVOLUMETO;
    }

    public String getLASTTRADEID() {
        return lASTTRADEID;
    }

    public void setLASTTRADEID(String lASTTRADEID) {
        this.lASTTRADEID = lASTTRADEID;
    }

    public Double getVOLUMEDAY() {
        return vOLUMEDAY;
    }

    public void setVOLUMEDAY(Double vOLUMEDAY) {
        this.vOLUMEDAY = vOLUMEDAY;
    }

    public Double getVOLUMEDAYTO() {
        return vOLUMEDAYTO;
    }

    public void setVOLUMEDAYTO(Double vOLUMEDAYTO) {
        this.vOLUMEDAYTO = vOLUMEDAYTO;
    }

    public Double getVOLUME24HOUR() {
        return vOLUME24HOUR;
    }

    public void setVOLUME24HOUR(Double vOLUME24HOUR) {
        this.vOLUME24HOUR = vOLUME24HOUR;
    }

    public Double getVOLUME24HOURTO() {
        return vOLUME24HOURTO;
    }

    public void setVOLUME24HOURTO(Double vOLUME24HOURTO) {
        this.vOLUME24HOURTO = vOLUME24HOURTO;
    }

    public Double getOPENDAY() {
        return oPENDAY;
    }

    public void setOPENDAY(Double oPENDAY) {
        this.oPENDAY = oPENDAY;
    }

    public Double getHIGHDAY() {
        return hIGHDAY;
    }

    public void setHIGHDAY(Double hIGHDAY) {
        this.hIGHDAY = hIGHDAY;
    }

    public Double getLOWDAY() {
        return lOWDAY;
    }

    public void setLOWDAY(Double lOWDAY) {
        this.lOWDAY = lOWDAY;
    }

    public Double getOPEN24HOUR() {
        return oPEN24HOUR;
    }

    public void setOPEN24HOUR(Double oPEN24HOUR) {
        this.oPEN24HOUR = oPEN24HOUR;
    }

    public Double getHIGH24HOUR() {
        return hIGH24HOUR;
    }

    public void setHIGH24HOUR(Double hIGH24HOUR) {
        this.hIGH24HOUR = hIGH24HOUR;
    }

    public Double getLOW24HOUR() {
        return lOW24HOUR;
    }

    public void setLOW24HOUR(Double lOW24HOUR) {
        this.lOW24HOUR = lOW24HOUR;
    }

    public String getLASTMARKET() {
        return lASTMARKET;
    }

    public void setLASTMARKET(String lASTMARKET) {
        this.lASTMARKET = lASTMARKET;
    }

    public Double getVOLUMEHOUR() {
        return vOLUMEHOUR;
    }

    public void setVOLUMEHOUR(Double vOLUMEHOUR) {
        this.vOLUMEHOUR = vOLUMEHOUR;
    }

    public Double getVOLUMEHOURTO() {
        return vOLUMEHOURTO;
    }

    public void setVOLUMEHOURTO(Double vOLUMEHOURTO) {
        this.vOLUMEHOURTO = vOLUMEHOURTO;
    }

    public Double getOPENHOUR() {
        return oPENHOUR;
    }

    public void setOPENHOUR(Double oPENHOUR) {
        this.oPENHOUR = oPENHOUR;
    }

    public Double getHIGHHOUR() {
        return hIGHHOUR;
    }

    public void setHIGHHOUR(Double hIGHHOUR) {
        this.hIGHHOUR = hIGHHOUR;
    }

    public Double getLOWHOUR() {
        return lOWHOUR;
    }

    public void setLOWHOUR(Double lOWHOUR) {
        this.lOWHOUR = lOWHOUR;
    }

    public Double getCHANGE24HOUR() {
        return cHANGE24HOUR;
    }

    public void setCHANGE24HOUR(Double cHANGE24HOUR) {
        this.cHANGE24HOUR = cHANGE24HOUR;
    }

    public Double getCHANGEPCT24HOUR() {
        return cHANGEPCT24HOUR;
    }

    public void setCHANGEPCT24HOUR(Double cHANGEPCT24HOUR) {
        this.cHANGEPCT24HOUR = cHANGEPCT24HOUR;
    }

    public Double getCHANGEDAY() {
        return cHANGEDAY;
    }

    public void setCHANGEDAY(Double cHANGEDAY) {
        this.cHANGEDAY = cHANGEDAY;
    }

    public Double getCHANGEPCTDAY() {
        return cHANGEPCTDAY;
    }

    public void setCHANGEPCTDAY(Double cHANGEPCTDAY) {
        this.cHANGEPCTDAY = cHANGEPCTDAY;
    }

    public Double getSUPPLY() {
        return sUPPLY;
    }

    public void setSUPPLY(Double sUPPLY) {
        this.sUPPLY = sUPPLY;
    }

    public Double getMKTCAP() {
        return mKTCAP;
    }

    public void setMKTCAP(Double mKTCAP) {
        this.mKTCAP = mKTCAP;
    }

    public Double getTOTALVOLUME24H() {
        return tOTALVOLUME24H;
    }

    public void setTOTALVOLUME24H(Double tOTALVOLUME24H) {
        this.tOTALVOLUME24H = tOTALVOLUME24H;
    }

    public Double getTOTALVOLUME24HTO() {
        return tOTALVOLUME24HTO;
    }

    public void setTOTALVOLUME24HTO(Double tOTALVOLUME24HTO) {
        this.tOTALVOLUME24HTO = tOTALVOLUME24HTO;
    }

    public String getIMAGEURL() {
        return iMAGEURL;
    }

    public void setIMAGEURL(String iMAGEURL) {
        this.iMAGEURL = iMAGEURL;
    }

}
