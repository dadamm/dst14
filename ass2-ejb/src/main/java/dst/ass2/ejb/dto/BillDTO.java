package dst.ass2.ejb.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class BillDTO implements Serializable {
	private static final long serialVersionUID = 1577495607705041680L;

	private List<BillPerTask> bills;
	private BigDecimal totalPrice;
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<BillPerTask> getBills() {
		return bills;
	}

	public void setBills(List<BillPerTask> bills) {
		this.bills = bills;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public class BillPerTask implements Serializable {
		private static final long serialVersionUID = 8656991004468599034L;

		private Long taskId;
		private Integer numberOfWorkers;
		private BigDecimal setupCosts;
		private BigDecimal processingCosts;
		private BigDecimal taskCosts;

		public Integer getNumberOfWorkers() {
			return numberOfWorkers;
		}
		public void setNumberOfWorkers(Integer numberOfWorkers) {
			this.numberOfWorkers = numberOfWorkers;
		}
		public BigDecimal getProcessingCosts() {
			return processingCosts;
		}
		public void setProcessingCosts(BigDecimal processingCosts) {
			this.processingCosts = processingCosts;
		}
		public BigDecimal getSetupCosts() {
			return setupCosts;
		}
		public void setSetupCosts(BigDecimal setupCosts) {
			this.setupCosts = setupCosts;
		}
		public BigDecimal getTaskCosts() {
			return taskCosts;
		}
		public void setTaskCosts(BigDecimal taskCosts) {
			this.taskCosts = taskCosts;
		}
		public Long getTaskId() {
			return taskId;
		}
		public void setTaskId(Long taskId) {
			this.taskId = taskId;
		}

		@Override
		public String toString() {
			return "BillPerTask [taskId=" + taskId + ", numberOfWorkers="
					+ numberOfWorkers + ", setupCosts=" + setupCosts
					+ ", processingCosts=" + processingCosts + ", taskCosts="
					+ taskCosts + "]";
		}

	}

	@Override
	public String toString() {
		return "BillDTO [bills=" + bills + ", totalPrice=" + totalPrice
				+ ", username=" + username + "]";
	}

}
