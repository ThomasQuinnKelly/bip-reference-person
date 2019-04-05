package gov.va.bip.vetservices.claims.orm;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Attributes implements Serializable {
	
	private static final long serialVersionUID = -4926174195580891727L;

	@Id
	@GeneratedValue
	private long id;

	private String date_filed;

	private String documents_needed;

	private String decision_letter_sent;

	private String updated_at;

	private String requested_decision;

	private String waiver_submitted;

	private String min_est_date;

	private String claim_type;

	private String max_est_date;

	private String open;

	private String development_letter_sent;

	private String status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDate_filed() {
		return date_filed;
	}

	public void setDate_filed(String date_filed) {
		this.date_filed = date_filed;
	}

	public String getDocuments_needed() {
		return documents_needed;
	}

	public void setDocuments_needed(String documents_needed) {
		this.documents_needed = documents_needed;
	}

	public String getDecision_letter_sent() {
		return decision_letter_sent;
	}

	public void setDecision_letter_sent(String decision_letter_sent) {
		this.decision_letter_sent = decision_letter_sent;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public String getWaiver_submitted() {
		return waiver_submitted;
	}

	public void setWaiver_submitted(String waiver_submitted) {
		this.waiver_submitted = waiver_submitted;
	}

	public String getMin_est_date() {
		return min_est_date;
	}

	public void setMin_est_date(String min_est_date) {
		this.min_est_date = min_est_date;
	}

	public String getMax_est_date() {
		return max_est_date;
	}

	public void setMax_est_date(String max_est_date) {
		this.max_est_date = max_est_date;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public String getDevelopment_letter_sent() {
		return development_letter_sent;
	}

	public void setDevelopment_letter_sent(String development_letter_sent) {
		this.development_letter_sent = development_letter_sent;
	}

	public String getRequested_decision() {
		return requested_decision;
	}

	public void setRequested_decision(String requested_decision) {
		this.requested_decision = requested_decision;
	}

	public String getClaim_type() {
		return claim_type;
	}

	public void setClaim_type(String claim_type) {
		this.claim_type = claim_type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ClassPojo [date_filed = " + date_filed + ", documents_needed = " + documents_needed
				+ ", decision_letter_sent = " + decision_letter_sent + ", updated_at = " + updated_at
				+ ", requested_decision = " + requested_decision + ", waiver_submitted = " + waiver_submitted
				+ ", min_est_date = " + min_est_date + ", claim_type = " + claim_type + ", max_est_date = "
				+ max_est_date + ", open = " + open + ", development_letter_sent = " + development_letter_sent
				+ ", status = " + status + "]";
	}
}