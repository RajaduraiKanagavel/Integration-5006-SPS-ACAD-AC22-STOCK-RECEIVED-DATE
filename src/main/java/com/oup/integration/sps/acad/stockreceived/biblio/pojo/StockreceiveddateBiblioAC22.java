package com.oup.integration.sps.acad.stockreceived.biblio.pojo;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"impressionNumber",
"deliveryDate",
"productISBN",
"publicationDate"
})
public class StockreceiveddateBiblioAC22 {
	

@JsonProperty("impressionNumber")
private String impressionNumber;


@JsonProperty("deliveryDate")
private String deliveryDate;


@JsonProperty("productISBN")
private String productISBN;


@JsonProperty("publicationDate")
private String publicationDate;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("impressionNumber")
public String getImpressionNumber() {
return impressionNumber;
}

@JsonProperty("impressionNumber")
public void setImpressionNumber(String impressionNumber) {
this.impressionNumber = impressionNumber;
}

@JsonProperty("deliveryDate")
public String getDeliveryDate() {
return deliveryDate;
}

@JsonProperty("deliveryDate")
public void setDeliveryDate(String deliveryDate) {
this.deliveryDate = deliveryDate;
}

@JsonProperty("productISBN")
public String getProductISBN() {
return productISBN;
}

@JsonProperty("productISBN")
public void setProductISBN(String productISBN) {
this.productISBN = productISBN;
}

@JsonProperty("publicationDate")
public String getPublicationDate() {
return publicationDate;
}

@JsonProperty("publicationDate")
public void setPublicationDate(String publicationDate) {
this.publicationDate = publicationDate;
}

@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

@JsonAnySetter
public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}