package passionx3.jkdk.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import passionx3.jkdk.exception.AttachFileException;
import passionx3.jkdk.domain.Online;

@Component
public class FileUtils {

//	https://congsong.tistory.com/39?category=749196

	/** 오늘 날짜 */
	private final String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));

	/** 업로드 경로 */
	private final String uploadPath = Paths.get("C:", "jkdk", "upload", today).toString();

	/**
	 * 서버에 생성할 파일명을 처리할 랜덤 문자열 반환
	 * 
	 * @return 랜덤 문자열
	 */
	private final String getRandomString() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 서버에 첨부 파일을 생성하고, 업로드 파일 목록 반환
	 * 
	 * @param files    - 파일 Array
	 * @param boardIdx - 게시글 번호
	 * @return 업로드 파일 목록
	 */

	public Online uploadFiles(MultipartFile file, Online online) {
		/* uploadPath에 해당하는 디렉터리가 존재하지 않으면, 부모 디렉터리를 포함한 모든 디렉터리를 생성 */
		File dir = new File(uploadPath);
		if (dir.exists() == false) {
			dir.mkdirs();
		}

		if (file.getSize() < 1) {
			return online;

		}
		try {
			/* form의 name */
			String formName = file.getName();

			/* 파일 확장자 */
			final String extension = FilenameUtils.getExtension(file.getOriginalFilename());
			/* 서버에 저장할 파일명 (랜덤 문자열 + 확장자) */
			final String saveName = getRandomString() + "." + extension;

			/* 업로드 경로에 saveName과 동일한 이름을 가진 파일 생성 */
			File target = new File(uploadPath, saveName);
			file.transferTo(target);

			/* 파일 정보 저장 */
			if (formName.equals("pcFile")) {
				online.setPcFile(saveName);
			} else if (formName.equals("phoneFile")) {
				online.setPhoneFile(saveName);
			} else if (formName.equals("tabletFile")) {
				online.setTabletFile(saveName);
			}

		} catch (IOException e) {
			throw new AttachFileException("[" + file.getOriginalFilename() + "] failed to save file...");

		} catch (Exception e) {
			throw new AttachFileException("[" + file.getOriginalFilename() + "] failed to save file...");
		}

		return online;
	}

	public Online uploadFiles(MultipartFile[] files, Online online) {
		/* uploadPath에 해당하는 디렉터리가 존재하지 않으면, 부모 디렉터리를 포함한 모든 디렉터리를 생성 */
		File dir = new File(uploadPath);
		if (dir.exists() == false) {
			dir.mkdirs();
		}

		int cnt = 0;

		/* 파일 개수만큼 forEach 실행 */
		for (MultipartFile file : files) {
			if (file.getSize() < 1) {
				cnt++;
				continue;
			}
			try {
				/* 파일 확장자 */
				final String extension = FilenameUtils.getExtension(file.getOriginalFilename());
				/* 서버에 저장할 파일명 (랜덤 문자열 + 확장자) */
				final String saveName = getRandomString() + "." + extension;

				/* 업로드 경로에 saveName과 동일한 이름을 가진 파일 생성 */
				File target = new File(uploadPath, saveName);
				file.transferTo(target);

				/* 파일 정보 저장 */
				if (cnt == 0) {
					online.setThumbnail1(saveName);
				} else if (cnt == 1) {
					online.setThumbnail2(saveName);
				} else if (cnt == 2) {
					online.setThumbnail3(saveName);
				}

				cnt++;
			} catch (IOException e) {
				throw new AttachFileException("[" + file.getOriginalFilename() + "] failed to save file...");

			} catch (Exception e) {
				throw new AttachFileException("[" + file.getOriginalFilename() + "] failed to save file...");
			}
		} // end of for

		return online;
	}
}
