package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency {
    private String CcyNm_EN;
    private String CcyNm_UZC;
    private String Diff;
    private String Rate;
    private String Ccy;
    private String CcyNm_RU;
    private int id;
    private String CcyNm_UZ;
    private String Code;
    private String Nominal;
    private String Date;
}