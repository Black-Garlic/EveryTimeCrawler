import java.util.ArrayList;

public class Section {
	private String sectionCode;
	private String professor;
	private String score;
	private String homework;
	private String teamProject;
	private String exam;
	private ArrayList<Review> reviewList = new ArrayList<>();;
	
	Section(String sectionCode) {
		this.sectionCode = sectionCode;
	}
	
	public void setInfo(String professor, String score, String homework, String teamProject, String exam) {
		this.professor = professor;
		this.score = score;
		this.homework = homework;
		this.teamProject = teamProject;
		this.exam = exam;
	}

	public String getProfessor() {
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor = professor;
	}

	public String getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getHomework() {
		return homework;
	}

	public void setHomework(String homework) {
		this.homework = homework;
	}

	public String getTeamProject() {
		return teamProject;
	}

	public void setTeamProject(String teamProject) {
		this.teamProject = teamProject;
	}

	public String getExam() {
		return exam;
	}

	public void setExam(String exam) {
		this.exam = exam;
	}
	
	public ArrayList<Review> getReviewList() {
		return reviewList;
	}

	public void setReviewList(ArrayList<Review> reviewList) {
		this.reviewList = reviewList;
	}

	public void addReview(int star, String review) {
		reviewList.add(new Review(star, review));
	}

	@Override
	public String toString() {
		return "Section [professor=" + professor + ", sectionCode=" + sectionCode + ", score=" + score + ", homework=" + homework
				+ ", teamProject=" + teamProject + ", exam=" + exam + "]";
	}
	
	
}
