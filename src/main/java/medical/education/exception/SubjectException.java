package medical.education.exception;

import spring.backend.library.exception.BaseException;
import spring.backend.library.msg.Message;

public class SubjectException extends BaseException{

  private final static int ERROR_CODE = 1000;

  public SubjectException(int code, String message) {
    super(code, message, null);
  }

  public static class SubjectNotExist extends SubjectException {

    public SubjectNotExist(Long id) {
      super(ERROR_CODE + 1, Message.getMessage("subject.not.exists", new Object[]{id}));
    }
  }
}
