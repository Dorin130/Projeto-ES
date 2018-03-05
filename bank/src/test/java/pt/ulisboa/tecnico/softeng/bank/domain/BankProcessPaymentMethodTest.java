package pt.ulisboa.tecnico.softeng.bank.domain;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class BankProcessPaymentMethodTest {

    private Bank bank;
    private Account account;

    @Before
    public void setUp() {
        this.bank = new Bank("Money", "BK01");
        Client client = new Client(this.bank, "Hugo");
        this.account = new Account(this.bank, client);
        this.account.deposit(100);
    }

    @Test
    public void success() {


        Bank bank = new Bank("Coin", "BK03");
        Client client = new Client(bank, "Manuel");
        Account account = new Account(bank, client);
        account.deposit(100);
        String reference1 = this.bank.processPayment(this.account.getIBAN(), 10);
        String reference2 = bank.processPayment(account.getIBAN(), 10);

        Operation operation1 = this.bank.getOperation(reference1);
        Assert.assertNotNull(operation1);
        Assert.assertEquals(this.account, operation1.getAccount());
        Assert.assertEquals(this.account.getBalance(), 90);


        Operation operation2 = this.bank.getOperation(reference2);
        Assert.assertNotNull(operation2);
        Assert.assertEquals(account, operation2.getAccount());
        Assert.assertEquals(account.getBalance(), 90);

    }

    @Test(expected = BankException.class)
    public void inexistentAccount() {
        this.bank.processPayment("BK021", 10);
    }

    @Test
    public void multipleAccountsInTheBank() {
        Client client = new Client(this.bank, "Manuel");
        new Account(this.bank, client);
        this.bank.processPayment(this.account.getIBAN(), 10);
        Assert.assertEquals(90, this.account.getBalance());
    }


    @Test(expected = BankException.class)
    public void nullAmount() {
        this.bank.processPayment(this.account.getIBAN(), 10);
    }

    @Test(expected = BankException.class)
    public void zeroAmount() {
        this.bank.processPayment(this.account.getIBAN(), 0);
    }

    @Test
    public void oneAmount() {
        this.bank.processPayment(this.account.getIBAN(), 1);
    }

    @Test(expected = BankException.class)
    public void notEnoughBalance() {
        this.bank.processPayment(this.account.getIBAN(), this.account.getBalance() + 1);
    }

    @Test
    public void allTheBalance() {
        this.bank.processPayment(this.account.getIBAN(), this.account.getBalance());
    }

    @Test(expected = BankException.class)
    public void negativeAmount() {
        this.bank.processPayment(this.account.getIBAN(), -100);
    }


    @After
    public void tearDown() {
        Bank.banks.clear();
    }

}
