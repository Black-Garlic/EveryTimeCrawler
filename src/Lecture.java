import java.util.ArrayList;

public class Lecture {
	private String topCategory;
	private String subCategory;
	private String lectureCode;
	private String lectureName;
	private ArrayList<Section> sectionList = new ArrayList<>();
	
	Lecture(String topCategory, String subCategory, String lectureCode, String lectureName) {
		this.topCategory = topCategory;
		this.subCategory = subCategory;
		this.lectureCode = lectureCode;
		this.lectureName = lectureName;
	}
	
	public String getTopCategory() {
		return topCategory;
	}


	public void setTopCategory(String topCategory) {
		this.topCategory = topCategory;
	}


	public String getSubCategory() {
		return subCategory;
	}


	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}


	public String getLectureCode() {
		return lectureCode;
	}


	public void setLectureCode(String lectureCode) {
		this.lectureCode = lectureCode;
	}


	public String getLectureName() {
		return lectureName;
	}


	public void setLectureName(String lectureName) {
		this.lectureName = lectureName;
	}


	public ArrayList<Section> getSectionList() {
		return sectionList;
	}


	public void setSectionList(ArrayList<Section> sectionList) {
		this.sectionList = sectionList;
	}


	public void addSection(String code) {
		sectionList.add(new Section(code));
	}
}
