package com.jasper.schoolbell.dtos;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class CallDto {
    @XmlElement(name = "Say")
    private Say say;

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Say {
        @XmlValue
        private String value;

        @XmlAttribute
        private boolean playBeep;

//        @XmlAttribute
//        private String voice = "en-US-Neural2-C";
    }

    public static CallDto buildSay(final String message) {
        final CallDto callDto = new CallDto();
        final Say sayObj = new Say();
        sayObj.setValue(message);
        callDto.setSay(sayObj);
        return callDto;
    }
}
