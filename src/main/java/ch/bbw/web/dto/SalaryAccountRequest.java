package ch.bbw.web.dto;

/**
 * Request to create a salary account
 *
 * @param creditLimit the credit limit in millirappen, 0 or negative
 * @author Cedric Caviezel, cedric.caviezel@stud.kbw.ch
 * @version 1.0
 */
public record SalaryAccountRequest(long creditLimit) {
}
