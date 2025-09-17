package snippet;

public class Snippet {
	Question question = this.questionService.getQuestion(id);
	
			if (!question.getAuthor().getUsername().equals(principal.getName())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다");
			}
	
			this.questionService.delete(question);
			return "redirect:/";
}

