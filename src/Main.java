
import java.util.ArrayList;
import java.io.*;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.google.gson.Gson;

public class Main {

	public static final String WEB_DRIVER_ID = "webdriver.chrome.driver"; // 드라이버 ID
	public static final String WEB_DRIVER_PATH = "C:\\Drivers\\chromedriver.exe"; // 드라이버 경로
	public static ArrayList<Lecture> lectureList = new ArrayList<Lecture>();
	public static String[] topCategoryList = {"ICT", "기초학문", "리더십 및 문제해결", "세계관", "소통 및 융복합", "신앙 2", "영어", "예체능", "외국어", "전공기초", "특론 및 개별연구"};

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		setLectureList();

		// 드라이버 설정
		try {
			System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 크롬 설정을 담은 객체 생성
		ChromeOptions options = new ChromeOptions();
		// 브라우저가 눈에 보이지 않고 내부적으로 돈다.
		// 설정하지 않을 시 실제 크롬 창이 생성되고, 어떤 순서로 진행되는지 확인할 수 있다.
		// options.addArguments("headless");

		// 위에서 설정한 옵션은 담은 드라이버 객체 생성
		// 옵션을 설정하지 않았을 때에는 생략 가능하다.
		// WebDriver객체가 곧 하나의 브라우저 창이라 생각한다.
		WebDriver driver = new ChromeDriver(options);

		driver.get("https://everytime.kr/login");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}

		WebElement loginId = driver.findElement(By.name("userid"));
		loginId.sendKeys("samuel1226");
		WebElement loginPassword = driver.findElement(By.name("password"));
		loginPassword.sendKeys("hn1020011");
		WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"container\"]/form/p[3]/input"));
		loginButton.click();

		// 브라우저 이동시 생기는 로드시간을 기다린다.
		// HTTP응답속도보다 자바의 컴파일 속도가 더 빠르기 때문에 임의적으로 1초를 대기한다.
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		
		String topCategory = topCategoryList[10];

		for (int lectureCount = 0; lectureCount < lectureList.size(); lectureCount++) {
			if (lectureList.get(lectureCount).getTopCategory().equals(topCategory)) {
				for (int sectionCount = 0; sectionCount < lectureList.get(lectureCount).getSectionList().size(); sectionCount++) {
					String url = "https://everytime.kr/lecture/view/"
							+ lectureList.get(lectureCount).getSectionList().get(sectionCount).getSectionCode();
					driver.get(url);

					System.out.println(lectureList.get(lectureCount).getLectureName());
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}

					WebElement lectureProfessor = driver.findElement(By.xpath("//*[@id=\"container\"]/div[2]/p[1]/span"));

					WebElement lectureScore = driver
							.findElement(By.xpath("//*[@id=\"container\"]/div[4]/div[1]/div[1]/span/span[1]"));

					WebElement lectureHomework = driver
							.findElement(By.xpath("//*[@id=\"container\"]/div[4]/div[1]/div[2]/p[1]/span"));

					WebElement lectureTeam = driver
							.findElement(By.xpath("//*[@id=\"container\"]/div[4]/div[1]/div[2]/p[2]/span"));
					
					WebElement lectureGrade = driver
							.findElement(By.xpath("//*[@id=\"container\"]/div[4]/div[1]/div[2]/p[3]/span"));

					WebElement lectureExam = driver
							.findElement(By.xpath("//*[@id=\"container\"]/div[4]/div[1]/div[2]/p[5]/span"));

					lectureList.get(lectureCount).getSectionList().get(sectionCount).setInfo(
							lectureProfessor.getText(),
							lectureScore.getText(),
							lectureHomework.getText(),
							lectureTeam.getText(),
							lectureGrade.getText(),
							lectureExam.getText());

					List<WebElement> article = driver.findElement(By.className("articles"))
							.findElements(By.tagName("article"));
					System.out.println(article.size());

					for (int reviewCount = 0; reviewCount < article.size(); reviewCount++) {
						System.out.println(reviewCount);
						int star = article.get(reviewCount).findElement(By.className("rate"))
								.findElement(By.className("star")).findElement(By.className("on")).getSize().width / 12;
						String review = article.get(reviewCount).findElement(By.className("text")).getText();
						lectureList.get(lectureCount).getSectionList().get(sectionCount).addReview(star, review);
					}
				}
			}
		}
		// 1초 대기
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		
		ArrayList<Lecture> tmpLectureList = new ArrayList<Lecture>();
		
		for (int lectureCount = 0; lectureCount < lectureList.size(); lectureCount++) {
			if (lectureList.get(lectureCount).getTopCategory().equals(topCategory)) {
				tmpLectureList.add(lectureList.get(lectureCount));
			}
		}
		
		
		Gson gson = new Gson();
		
		
		String lectureJson = gson.toJson(tmpLectureList);

		System.out.println(lectureJson);
		
//		for (int lectureCount = 0; lectureCount < 30; lectureCount++) {
//			String lectureName = lectureList.get(lectureCount).getLectureName();
//
//			for (int sectionCount = 0; sectionCount < lectureList.get(lectureCount).getSectionList().size(); sectionCount++) {
//				String lectureProfessor = lectureList.get(lectureCount).getSectionList().get(sectionCount).getProfessor();
//
//				for (int reviewCount = 0; reviewCount < lectureList.get(lectureCount).getSectionList().get(sectionCount)
//						.getReviewList().size(); reviewCount++) {
//					int star = lectureList.get(lectureCount).getSectionList().get(sectionCount).getReviewList()
//							.get(reviewCount).getStar();
//					String review = lectureList.get(lectureCount).getSectionList().get(sectionCount).getReviewList()
//							.get(reviewCount).getReview();
//					//System.out.println(lectureName + " - " + lectureProfessor + " - " + star + " / " + review);
//					// System.out.println("\"\"\"" + review + "\"\"\""););
//				}
//			}
//			//System.out.println("\n\n\n");
//		}

		try {
			// 드라이버가 null이 아니라면
			if (driver != null) {
				// 드라이버 연결 종료
				driver.close(); // 드라이버 연결 해제

				// 프로세스 종료
				driver.quit();
			}
			
			String filePath = topCategory + ".txt";
			
			FileWriter fileWriter = new FileWriter(filePath);
			fileWriter.write(lectureJson);
			fileWriter.close();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}

	}

	public static void setLectureList() {
		// 0
		lectureList.add(new Lecture(topCategoryList[0], "ICT 입문", "GCS10001", "소프트웨어 입문"));
		lectureList.add(new Lecture(topCategoryList[0], "소프트웨어 활용", "GCS10007", "오피스 활용"));
		lectureList.add(new Lecture(topCategoryList[0], "소프트웨어 활용", "GCS20005", "데이터 통계 처리"));
		lectureList.add(new Lecture(topCategoryList[0], "소프트웨어 활용", "GCS20008", "컴퓨터 그래픽 기초"));
		lectureList.add(new Lecture(topCategoryList[0], "소프트웨어 활용", "GCS20009", "하이브리드 웹 설계"));
		lectureList.add(new Lecture(topCategoryList[0], "소프트웨어 활용", "GCS20010", "데이터 수집과 응용"));
		lectureList.add(new Lecture(topCategoryList[0], "프로그래밍 기초", "GCS10003", "앱 프로그래밍"));
		lectureList.add(new Lecture(topCategoryList[0], "프로그래밍 기초", "GCS10004", "파이썬 프로그래밍"));
		lectureList.add(new Lecture(topCategoryList[0], "프로그래밍 기초", "GCS10058", "C 프로그래밍"));
		lectureList.add(new Lecture(topCategoryList[0], "프로그래밍 기초", "GCS10080", "R을 이용한 빅데이터 분석"));
		// 10
		lectureList.add(new Lecture(topCategoryList[1], "사회과학", "GEK20042", "교육학개론"));
		lectureList.add(new Lecture(topCategoryList[1], "사회과학", "GEK10104", "글로벌리제이션과 한국의 대중문화"));
		lectureList.add(new Lecture(topCategoryList[1], "사회과학", "GEK10040", "사회학개론"));
		lectureList.add(new Lecture(topCategoryList[1], "사회과학", "GEK20044", "시민생활과 법"));
		lectureList.add(new Lecture(topCategoryList[1], "사회과학", "GEK10100", "통일한국개론"));
		lectureList.add(new Lecture(topCategoryList[1], "인문학", "GEK10035", "Korean History"));
		lectureList.add(new Lecture(topCategoryList[1], "인문학", "GEK10087", "동양의 역사와 문화"));
		lectureList.add(new Lecture(topCategoryList[1], "인문학", "GEK10088", "서양의 역사와 문화"));
		lectureList.add(new Lecture(topCategoryList[1], "인문학", "GEK10039", "역사와 인간"));
		lectureList.add(new Lecture(topCategoryList[1], "인문학", "GEK10035", "한국사(근현대사)"));
		// 20
		lectureList.add(new Lecture(topCategoryList[1], "인문학", "GEK10030", "철학개론"));
		lectureList.add(new Lecture(topCategoryList[1], "인문학", "GEK10033", "한국문학의 이해"));
		lectureList.add(new Lecture(topCategoryList[1], "인문학", "GEK10020", "한국의 전통문화"));
		lectureList.add(new Lecture(topCategoryList[1], "인문학", "GEK10101", "한국학개론"));
		lectureList.add(new Lecture(topCategoryList[1], "인문학", "GEK30030", "현대과학과 기술의 철학"));
		lectureList.add(new Lecture(topCategoryList[1], "인문학", "CCE23005", "한국어학개론"));
		lectureList.add(new Lecture(topCategoryList[1], "자연과학", "GEK10095", "Calculus 1"));
		lectureList.add(new Lecture(topCategoryList[1], "자연과학", "GEK10096", "Calculus 2"));
		lectureList.add(new Lecture(topCategoryList[1], "자연과학", "GEK10097", "Calculus 3"));
		lectureList.add(new Lecture(topCategoryList[1], "자연과학", "GEK10081", "공학수학"));
		// 30
		lectureList.add(new Lecture(topCategoryList[1], "자연과학", "GEK10053", "미분방정식과 응용"));
		lectureList.add(new Lecture(topCategoryList[1], "자연과학", "GEK10082", "선형대수학"));
		lectureList.add(new Lecture(topCategoryList[1], "자연과학", "GEK20053", "통계학"));
		lectureList.add(new Lecture(topCategoryList[1], "자연과학", "GEK10055", "물리학 1"));
		lectureList.add(new Lecture(topCategoryList[1], "자연과학", "GEK10056", "물리학 2"));
		lectureList.add(new Lecture(topCategoryList[1], "자연과학", "GEK10090", "물리학 개론"));
		lectureList.add(new Lecture(topCategoryList[1], "자연과학", "GEK10038", "물리학 실험 1"));
		lectureList.add(new Lecture(topCategoryList[1], "자연과학", "GEK10070", "생활 속의 생명과학"));
		lectureList.add(new Lecture(topCategoryList[1], "자연과학", "GEK10057", "일반생물학"));
		lectureList.add(new Lecture(topCategoryList[1], "자연과학", "GEK10058", "일반화학"));
		// 40
		lectureList.add(new Lecture(topCategoryList[1], "자연과학", "GEK10094", "일반화학실험"));
		lectureList.add(new Lecture(topCategoryList[2], "리더십", "GEK10080", "대학생을 위한 실용금융"));
		lectureList.add(new Lecture(topCategoryList[2], "리더십", "GEK30022", "리더십의 이해"));
		lectureList.add(new Lecture(topCategoryList[2], "리더십", "GEK10076", "비전과 진로탐색"));
		lectureList.add(new Lecture(topCategoryList[2], "리더십", "GEK20049", "생애진로설계"));
		lectureList.add(new Lecture(topCategoryList[2], "리더십", "GEK20024", "세계문화의 이해"));
		lectureList.add(new Lecture(topCategoryList[2], "리더십", "GEK20028", "인간관계와 자기성장"));
		lectureList.add(new Lecture(topCategoryList[2], "리더십", "GEK30007", "직업과 소명"));
		lectureList.add(new Lecture(topCategoryList[2], "리더십", "GEK20074", "창의학습"));
		lectureList.add(new Lecture(topCategoryList[2], "리더십", "GEK10110", "한국수어"));
		// 50
		lectureList.add(new Lecture(topCategoryList[2], "리더십", "GEE30023", "Developing Leadership for a Global World"));
		lectureList.add(new Lecture(topCategoryList[3], "세계관 1", "GEK10011", "창조와 진화"));
		lectureList.add(new Lecture(topCategoryList[3], "세계관 1", "GEK20011", "기독교세계관"));
		lectureList.add(new Lecture(topCategoryList[3], "세계관 1", "GEK20051", "Mission Perspective"));
		lectureList.add(new Lecture(topCategoryList[3], "세계관 2", "GEK20016", "신앙특론"));
		lectureList.add(new Lecture(topCategoryList[3], "세계관 2", "GEK20019", "교회사의 이해"));
		lectureList.add(new Lecture(topCategoryList[3], "세계관 2", "GEK20035", "신앙과 학문의 통합"));
		lectureList.add(new Lecture(topCategoryList[3], "세계관 2", "GEK20061", "기독교 윤리"));
		lectureList.add(new Lecture(topCategoryList[3], "세계관 2", "GEK20062", "기독교 변증학"));
		lectureList.add(new Lecture(topCategoryList[3], "세계관 2", "GEK30014", "기독교와 현대사상"));
		// 60
		lectureList.add(new Lecture(topCategoryList[3], "세계관 2", "GEK30015", "현대사회와 선교"));
		lectureList.add(new Lecture(topCategoryList[4], "소통", "GCS10010", "대학글쓰기 기초"));
		lectureList.add(new Lecture(topCategoryList[4], "소통", "GCS10011", "이공계 글쓰기"));
		lectureList.add(new Lecture(topCategoryList[4], "소통", "GCS10012", "인문사회계 글쓰기"));
		lectureList.add(new Lecture(topCategoryList[4], "소통", "GCS10014", "논리와 비판적 사고"));
		lectureList.add(new Lecture(topCategoryList[4], "소통", "GCS10015", "토론과 발표"));
		lectureList.add(new Lecture(topCategoryList[4], "소통", "GCS10037", "중국어 1"));
		lectureList.add(new Lecture(topCategoryList[4], "소통", "GCS10038", "중국어 2"));
		lectureList.add(new Lecture(topCategoryList[4], "소통", "GCS20054", "한국어 5"));
		lectureList.add(new Lecture(topCategoryList[4], "소통", "GCS20055", "한국어 6"));
		// 70
		lectureList.add(new Lecture(topCategoryList[4], "융복합", "GCS10016", "고전강독"));
		lectureList.add(new Lecture(topCategoryList[4], "융복합", "GCS10065", "AI 이노베이션"));
		lectureList.add(new Lecture(topCategoryList[4], "융복합", "GEK10007", "교양독서"));
		lectureList.add(new Lecture(topCategoryList[4], "융복합", "GEK10032", "과학기술과 인간정신"));
		lectureList.add(new Lecture(topCategoryList[4], "융복합", "GEK10059", "환경과 인간"));
		lectureList.add(new Lecture(topCategoryList[4], "융복합", "GEK10102", "질병과 건강"));
		lectureList.add(new Lecture(topCategoryList[4], "융복합", "GEK30045", "현대사회와 여성"));
		lectureList.add(new Lecture(topCategoryList[4], "융복합", "GCS10066", "중국 문화와 언어"));
		lectureList.add(new Lecture(topCategoryList[5], "기독교 신앙의 기초 1", "GEK20058", "성경의 이해"));
		lectureList.add(new Lecture(topCategoryList[5], "기독교 신앙의 기초 2", "GEK20059", "기독교의 이해"));
		// 80
		lectureList.add(new Lecture(topCategoryList[5], "기독교 신앙의 기초 2", "GEK20060", "기독교와 포스트 모더니즘"));
		lectureList.add(new Lecture(topCategoryList[5], "기독교 신앙의 기초 2", "GEK20065", "기독교와 비교종교"));
		lectureList.add(new Lecture(topCategoryList[6], "Level 1", "GCS10053", "English Communication"));
		lectureList.add(new Lecture(topCategoryList[6], "Level 2", "GCS20003", "English Reading and Composition"));
		lectureList.add(new Lecture(topCategoryList[6], "Level 3", "GCS30008", "EAP - Engineering"));
		lectureList.add(new Lecture(topCategoryList[6], "Level 3", "GCS30009", "EAP - Life Science"));
		lectureList.add(new Lecture(topCategoryList[6], "Level 3", "GCS30010", "EAP - Management and Economics"));
		lectureList.add(new Lecture(topCategoryList[6], "Level 3", "GCS30011", "EAP - Counseling Psychology and Social Welfare"));
		lectureList.add(new Lecture(topCategoryList[6], "Level 3", "GCS30012", "EAP - Communication Arts and Science"));
		lectureList.add(new Lecture(topCategoryList[6], "Level 3", "GCS30013", "EAP - Contents Convergence Design"));
		// 90
		lectureList.add(new Lecture(topCategoryList[6], "Level 3", "GCS30014", "EAP - International Studies and Law"));
		lectureList.add(new Lecture(topCategoryList[6], "Level 3", "GCS30015", "EAP - Information Technology"));
		lectureList.add(new Lecture(topCategoryList[6], "Level 3", "GCS30016", "EAP - Humanities"));
		lectureList.add(new Lecture(topCategoryList[6], "Remedial", "GCS10052", "English Foundation"));
		lectureList.add(new Lecture(topCategoryList[6], "영어 2", "GCS10008", "English Pre-Course 1"));
		lectureList.add(new Lecture(topCategoryList[6], "영어 2", "GCS10009", "English Pre-Course 2"));
		lectureList.add(new Lecture(topCategoryList[6], "영어 2", "GCS20011", "영작문"));
		lectureList.add(new Lecture(topCategoryList[6], "영어 2", "GCS40001", "Essentials of English Communication"));
		lectureList.add(new Lecture(topCategoryList[7], "스포츠", "GEK10021", "생활체육 1"));
		lectureList.add(new Lecture(topCategoryList[7], "스포츠", "GEK20021", "생활체육 2"));
		// 100
		lectureList.add(new Lecture(topCategoryList[7], "예술", "GEK10061", "음악의 이해"));
		lectureList.add(new Lecture(topCategoryList[7], "예술", "GEK10062", "미술의 이해"));
		lectureList.add(new Lecture(topCategoryList[7], "예술", "GEK10065", "영상의 이해"));
		lectureList.add(new Lecture(topCategoryList[7], "예술", "GEK10098", "교회음악 연습"));
		lectureList.add(new Lecture(topCategoryList[8], "제 2 외국어", "GCS10019", "독일어 1"));
		lectureList.add(new Lecture(topCategoryList[8], "제 2 외국어", "GCS10027", "스페인어 1"));
		lectureList.add(new Lecture(topCategoryList[8], "제 2 외국어", "GCS10033", "일본어 1"));
		lectureList.add(new Lecture(topCategoryList[8], "제 2 외국어", "GCS10034", "일본어 2"));
		lectureList.add(new Lecture(topCategoryList[8], "제 2 외국어", "GCS10047", "히브리어 1"));
		lectureList.add(new Lecture(topCategoryList[9], "전공기초", "CCC20053", "현대사회와 대중매체"));
		// 110
		lectureList.add(new Lecture(topCategoryList[9], "전공기초", "CCC20072", "영화의 이해"));
		lectureList.add(new Lecture(topCategoryList[9], "전공기초", "CSW10003", "심리학개론"));
		lectureList.add(new Lecture(topCategoryList[9], "전공기초", "CSW10005", "사회복지개론"));
		lectureList.add(new Lecture(topCategoryList[9], "전공기초", "CUE10004", "공간학 입문"));
		lectureList.add(new Lecture(topCategoryList[9], "전공기초", "ECE10020", "공학설계 입문"));
		lectureList.add(new Lecture(topCategoryList[9], "전공기초", "GEK10108", "공작"));
		lectureList.add(new Lecture(topCategoryList[9], "전공기초", "GEK10109", "모두를 위한 인공지능의 활용"));
		lectureList.add(new Lecture(topCategoryList[9], "전공기초", "IID10005", "드로잉기초 2"));
		lectureList.add(new Lecture(topCategoryList[9], "전공기초", "ISE10002", "언어학개론"));
		lectureList.add(new Lecture(topCategoryList[9], "전공기초", "ISE10051", "정치학개론"));
		// 120
		lectureList.add(new Lecture(topCategoryList[9], "전공기초", "ISE10052", "국제관계학입문"));
		lectureList.add(new Lecture(topCategoryList[9], "전공기초", "LAW10004", "법학입문"));
		lectureList.add(new Lecture(topCategoryList[9], "전공기초", "MEC10001", "경제학 입문"));
		lectureList.add(new Lecture(topCategoryList[9], "전공기초", "MEC10002", "경영학 입문"));
		lectureList.add(new Lecture(topCategoryList[9], "전공기초", "SIT21003", "글로벌 기업가정신입문"));
		lectureList.add(new Lecture(topCategoryList[9], "전공기초", "SIT22002", "ICT 융합입문"));
		lectureList.add(new Lecture(topCategoryList[9], "전공기초", "UIL30051", "Survey of American Law"));
		lectureList.add(new Lecture(topCategoryList[10], "특론", "GEK40091", "교양특론 2"));

		setSectionList();
	}

	static void setSectionList() {
		lectureList.get(0).addSection("1974665");
		lectureList.get(0).addSection("1974667");
		lectureList.get(0).addSection("1974666");
		lectureList.get(0).addSection("1974668");
		lectureList.get(1).addSection("1974673");
		lectureList.get(1).addSection("2049335");
		lectureList.get(3).addSection("1974720");
		lectureList.get(4).addSection("1974722");
		lectureList.get(5).addSection("2049363");
		lectureList.get(6).addSection("2049332");
		lectureList.get(6).addSection("2049331");
		lectureList.get(7).addSection("2049334");
		lectureList.get(7).addSection("1974672");
		lectureList.get(8).addSection("1974708");
		lectureList.get(8).addSection("2286395");
		lectureList.get(8).addSection("2049229");
		lectureList.get(8).addSection("2215796");
		lectureList.get(8).addSection("2126594");
		lectureList.get(8).addSection("2215795");
		lectureList.get(8).addSection("2049356");
		lectureList.get(8).addSection("1974707");
		lectureList.get(8).addSection("2049355");
		lectureList.get(8).addSection("2049228");
		lectureList.get(9).addSection("2049358");
		lectureList.get(10).addSection("1974830");
		lectureList.get(10).addSection("1974831");
		lectureList.get(10).addSection("2215644");
		lectureList.get(11).addSection("2049389");
		lectureList.get(12).addSection("1974773");
		lectureList.get(13).addSection("1974832");
		lectureList.get(14).addSection("2126615");
		lectureList.get(14).addSection("2286381");
		lectureList.get(14).addSection("1974806");
		lectureList.get(14).addSection("2049387");
		lectureList.get(16).addSection("1974793");
		lectureList.get(17).addSection("1974794");
		lectureList.get(17).addSection("2126610");
		lectureList.get(17).addSection("2126611");
		lectureList.get(19).addSection("2215642");
		lectureList.get(19).addSection("1974772");
		lectureList.get(20).addSection("1974769");
		lectureList.get(20).addSection("2286380");
		lectureList.get(21).addSection("1974771");
		lectureList.get(22).addSection("1974764");
		lectureList.get(23).addSection("1974807");
		lectureList.get(24).addSection("1974855");
		lectureList.get(25).addSection("2215708");
		lectureList.get(25).addSection("2049275");
		lectureList.get(26).addSection("2286563");
		lectureList.get(26).addSection("2286382");
		lectureList.get(26).addSection("1974800");
		lectureList.get(26).addSection("1974799");
		lectureList.get(26).addSection("2286564");
		lectureList.get(26).addSection("1974798");
		lectureList.get(27).addSection("1974802");
		lectureList.get(27).addSection("2126613");
		lectureList.get(27).addSection("2049384");
		lectureList.get(27).addSection("2215647");
		lectureList.get(27).addSection("1974801");
		lectureList.get(27).addSection("2286383");
		lectureList.get(28).addSection("1974803");
		lectureList.get(29).addSection("2215646");
		lectureList.get(29).addSection("2049382");
		lectureList.get(30).addSection("1974776");
		lectureList.get(30).addSection("1974775");
		lectureList.get(31).addSection("1974792");
		lectureList.get(32).addSection("1974836");
		lectureList.get(33).addSection("1974777");
		lectureList.get(33).addSection("2126607");
		lectureList.get(34).addSection("2049377");
		lectureList.get(35).addSection("1974795");
		lectureList.get(35).addSection("2126612");
		lectureList.get(36).addSection("2049374");
		lectureList.get(37).addSection("2350544");
		lectureList.get(37).addSection("1974787");
		lectureList.get(38).addSection("1974779");
		lectureList.get(38).addSection("1974778");
		lectureList.get(38).addSection("2126608");
		lectureList.get(38).addSection("2215645");
		lectureList.get(39).addSection("1974781");
		lectureList.get(39).addSection("1974780");
		lectureList.get(40).addSection("1974797");
		lectureList.get(41).addSection("1974791");
		lectureList.get(42).addSection("1974854");
		lectureList.get(42).addSection("2215648");
		lectureList.get(43).addSection("2126609");
		lectureList.get(43).addSection("1974789");
		lectureList.get(44).addSection("2126621");

		lectureList.get(46).addSection("2126619");
		lectureList.get(46).addSection("1974828");
		lectureList.get(46).addSection("1974827");
		lectureList.get(46).addSection("1974826");
		lectureList.get(47).addSection("1974849");
		lectureList.get(47).addSection("2049408");

		lectureList.get(49).addSection("2126617");

		lectureList.get(51).addSection("1974756");
		lectureList.get(51).addSection("2215630");
		lectureList.get(51).addSection("1974755");
		lectureList.get(51).addSection("1974757");
		lectureList.get(51).addSection("2286375");
		lectureList.get(51).addSection("2286376");
		lectureList.get(52).addSection("2126618");
		lectureList.get(52).addSection("1974817");
		lectureList.get(53).addSection("1974835");
		lectureList.get(53).addSection("2215631");
		lectureList.get(54).addSection("2286378");
		lectureList.get(54).addSection("1974818");
		lectureList.get(54).addSection("1974819");
		lectureList.get(55).addSection("1974821");
		lectureList.get(56).addSection("2215633");
		lectureList.get(57).addSection("2126623");
		lectureList.get(58).addSection("1974845");
		lectureList.get(58).addSection("2049406");
		lectureList.get(59).addSection("1974852");
		lectureList.get(60).addSection("2215634");
		lectureList.get(60).addSection("1974853");
		lectureList.get(61).addSection("2286390");
		lectureList.get(61).addSection("2215658");
		lectureList.get(61).addSection("2126587");
		lectureList.get(61).addSection("1974675");
		lectureList.get(62).addSection("2126588");
		lectureList.get(63).addSection("2126589");
		lectureList.get(63).addSection("1974676");
		lectureList.get(64).addSection("1974677");
		lectureList.get(65).addSection("2286391");
		lectureList.get(66).addSection("2215870");
		lectureList.get(66).addSection("1974686");
		lectureList.get(66).addSection("1974685");
		lectureList.get(66).addSection("1974687");
		lectureList.get(67).addSection("2215639");
		lectureList.get(67).addSection("2049342");
		lectureList.get(67).addSection("2215640");

		lectureList.get(69).addSection("2215660");
		lectureList.get(70).addSection("1974678");
		lectureList.get(70).addSection("2049338");
		lectureList.get(71).addSection("2126596");
		lectureList.get(72).addSection("1974747");
		lectureList.get(72).addSection("1974749");
		lectureList.get(72).addSection("1974752");
		lectureList.get(72).addSection("1974751");
		lectureList.get(72).addSection("2126604");
		lectureList.get(73).addSection("1974770");
		lectureList.get(74).addSection("2049378");
		lectureList.get(74).addSection("1974783");
		lectureList.get(74).addSection("1974782");
		lectureList.get(74).addSection("1974784");
		lectureList.get(75).addSection("2126616");
		lectureList.get(75).addSection("1974808");
		lectureList.get(76).addSection("1974856");
		lectureList.get(76).addSection("2286394");

		lectureList.get(78).addSection("2126622");
		lectureList.get(78).addSection("1974841");
		lectureList.get(78).addSection("1974838");
		lectureList.get(78).addSection("2049401");
		lectureList.get(78).addSection("2286389");
		lectureList.get(78).addSection("1974839");
		lectureList.get(78).addSection("1974842");
		lectureList.get(78).addSection("1974840");
		lectureList.get(79).addSection("1974843");
		lectureList.get(79).addSection("2049404");
		lectureList.get(79).addSection("2049402");
		lectureList.get(79).addSection("2049403");
		lectureList.get(79).addSection("2215657");
		lectureList.get(79).addSection("1974844");
		lectureList.get(80).addSection("2049405");
		lectureList.get(81).addSection("1974846");
		lectureList.get(82).addSection("1974701");
		lectureList.get(82).addSection("2049348");
		lectureList.get(82).addSection("2049349");
		lectureList.get(82).addSection("2049350");
		lectureList.get(82).addSection("1974702");
		lectureList.get(82).addSection("1974700");
		lectureList.get(82).addSection("1974699");
		lectureList.get(82).addSection("2350660");
		lectureList.get(82).addSection("2049345");
		lectureList.get(82).addSection("2215651");
		lectureList.get(82).addSection("2215650");
		lectureList.get(83).addSection("1974710");
		lectureList.get(83).addSection("1974717");
		lectureList.get(83).addSection("2049359");
		lectureList.get(83).addSection("2215863");
		lectureList.get(83).addSection("2286386");
		lectureList.get(83).addSection("1974712");
		lectureList.get(83).addSection("1974711");
		lectureList.get(83).addSection("1974715");
		lectureList.get(83).addSection("2286387");
		lectureList.get(83).addSection("2126597");
		lectureList.get(83).addSection("2126599");
		lectureList.get(83).addSection("2215653");
		lectureList.get(83).addSection("2126598");
		lectureList.get(83).addSection("2215654");
		lectureList.get(84).addSection("2215655");
		lectureList.get(84).addSection("2126603");
		lectureList.get(85).addSection("1974725");
		lectureList.get(86).addSection("2049366");
		lectureList.get(87).addSection("2049367");
		lectureList.get(88).addSection("1974729");
		lectureList.get(89).addSection("1974730");
		lectureList.get(90).addSection("2049368");
		lectureList.get(91).addSection("1974733");
		lectureList.get(92).addSection("1974734");
		lectureList.get(92).addSection("2286388");
		lectureList.get(93).addSection("2286384");
		lectureList.get(93).addSection("1974692");
		lectureList.get(93).addSection("1974695");
		lectureList.get(93).addSection("2126590");
		lectureList.get(93).addSection("2126593");
		lectureList.get(93).addSection("2215649");
		lectureList.get(93).addSection("1974693");
		lectureList.get(93).addSection("2126592");
		lectureList.get(93).addSection("2286385");
		lectureList.get(93).addSection("1974697");
		lectureList.get(93).addSection("2126591");

		lectureList.get(96).addSection("2126600");

		lectureList.get(98).addSection("1974765");
		lectureList.get(98).addSection("1974767");
		lectureList.get(98).addSection("1974766");
		lectureList.get(99).addSection("1974822");
		lectureList.get(99).addSection("1974823");
		lectureList.get(99).addSection("1974824");
		lectureList.get(100).addSection("1974785");
		lectureList.get(101).addSection("1974786");
		lectureList.get(102).addSection("2049381");
		lectureList.get(103).addSection("1974805");
		lectureList.get(103).addSection("2126614");
		lectureList.get(104).addSection("1974682");
		lectureList.get(105).addSection("1974683");
		lectureList.get(106).addSection("1974684");
		lectureList.get(107).addSection("2215638");
		lectureList.get(108).addSection("1974689");
		lectureList.get(109).addSection("1974506");
		lectureList.get(110).addSection("2049210");
		lectureList.get(110).addSection("2215724");
		lectureList.get(111).addSection("1974450");
		lectureList.get(111).addSection("2049159");
		lectureList.get(111).addSection("1974447");
		lectureList.get(111).addSection("2215757");
		lectureList.get(111).addSection("1974448");
		lectureList.get(112).addSection("2049160");
		lectureList.get(113).addSection("2215826");
		lectureList.get(114).addSection("2215825");
		lectureList.get(114).addSection("2049235");
		lectureList.get(114).addSection("2049234");
		lectureList.get(114).addSection("2049071");
		lectureList.get(114).addSection("2049236");
		lectureList.get(114).addSection("2350633");
		lectureList.get(115).addSection("2049390");
		lectureList.get(115).addSection("1974811");
		lectureList.get(116).addSection("1974812");
		lectureList.get(117).addSection("2049291");
		lectureList.get(117).addSection("2215841");
		lectureList.get(118).addSection("1974364");
		lectureList.get(118).addSection("2286413");
		lectureList.get(119).addSection("1974366");
		lectureList.get(119).addSection("2126668");
		lectureList.get(120).addSection("2286561");
		lectureList.get(120).addSection("2215637");
		lectureList.get(120).addSection("1974368");
		lectureList.get(120).addSection("2049097");
		lectureList.get(120).addSection("1974369");
		lectureList.get(121).addSection("1974419");
		lectureList.get(121).addSection("2049132");
		lectureList.get(122).addSection("1974290");
		lectureList.get(122).addSection("2049047");
		lectureList.get(122).addSection("1974291");
		lectureList.get(123).addSection("1974292");
		lectureList.get(123).addSection("2126638");
		lectureList.get(123).addSection("2049048");
		lectureList.get(123).addSection("1974294");
		lectureList.get(124).addSection("1974637");
		lectureList.get(124).addSection("1974636");
		lectureList.get(125).addSection("2215789");
		lectureList.get(125).addSection("2049317");
		lectureList.get(126).addSection("1974440");
		lectureList.get(127).addSection("2215661");
		lectureList.get(127).addSection("1974864");
		lectureList.get(127).addSection("1974865");
	}
}
