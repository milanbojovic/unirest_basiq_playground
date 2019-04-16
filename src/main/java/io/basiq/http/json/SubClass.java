package io.basiq.http.json;

@SuppressWarnings("unused")
public class SubClass {
	private String code;

	private String title;

	public SubClass(String code, String title) {
		this.code = code;
		this.title = title;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "SubClass [code=" + code + ", title=" + title + "]";
	}
}