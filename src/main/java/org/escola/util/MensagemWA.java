package org.escola.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MensagemWA {

	private String text;
	private boolean fromMe;
	private long timestamp;
	private String messageId;
	private String pushName;

	public MensagemWA(String text, boolean fromMe, long timestamp) {
		this.text = text;
		this.fromMe = fromMe;
		this.timestamp = timestamp;
	}

	public MensagemWA(String text, boolean fromMe, long timestamp, String messageId) {
		this.text = text;
		this.fromMe = fromMe;
		this.timestamp = timestamp;
		this.messageId = messageId;
	}

	public MensagemWA(String text, boolean fromMe, long timestamp, String messageId, String pushName) {
		this.text = text;
		this.fromMe = fromMe;
		this.timestamp = timestamp;
		this.messageId = messageId;
		this.pushName = pushName;
	}

	public String getText() { return text; }
	public boolean isFromMe() { return fromMe; }
	public boolean isPrimeiro() { return !fromMe; }
	public long getTimestamp() { return timestamp; }
	public String getMessageId() { return messageId; }
	public String getPushName() { return pushName != null ? pushName : ""; }

	public String getDataFormatada() {
		if (timestamp <= 0) return "";
		try {
			Date d = new Date(timestamp * 1000L);
			return new SimpleDateFormat("dd/MM/yy HH:mm").format(d);
		} catch (Exception e) {
			return "";
		}
	}
}
