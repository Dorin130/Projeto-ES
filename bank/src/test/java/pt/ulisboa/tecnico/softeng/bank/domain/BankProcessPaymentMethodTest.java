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
        String reference1 = Bank.processPayment(this.account.getIBAN(), 10);
        String reference2 = Bank.processPayment(account.getIBAN(), 10);

        Operation operation1 = this.bank.getOperation(reference1);
        Assert.assertNotNull(operation1);
        Assert.assertEquals(this.account, operation1.getAccount());
        Assert.assertEquals(this.account.getBalance(), 90);


        Operation operation2 = bank.getOperation(reference2);
        Assert.assertNotNull(operation2);
        Assert.assertEquals(account, operation2.getAccount());
        Assert.assertEquals(account.getBalance(), 90);

    }

    @Test(expected = BankException.class)
    public void inexistentAccount() {
        Bank.processPayment("BK021", 10);
    }

    @Test
    public void multipleAccountsInTheBank() {
        Client client = new Client(this.bank, "Manuel");
        new Account(this.bank, client);
        Bank.processPayment(this.account.getIBAN(), 10);
        Assert.assertEquals(90, this.account.getBalance());
    }


    @Test(expected = BankException.class)
    public void nullAmount() {
        Bank.processPayment(this.account.getIBAN(), 10);
    }

    @Test(expected = BankException.class)
    public void zeroAmount() {
        Bank.processPayment(this.account.getIBAN(), 0);
    }

    @Test
    public void oneAmount() {
        Bank.processPayment(this.account.getIBAN(), 1);
    }

    @Test(expected = BankException.class)
    public void notEnoughBalance() {
        Bank.processPayment(this.account.getIBAN(), this.account.getBalance() + 1);
    }

    @Test
    public void allTheBalance() {
        Bank.processPayment(this.account.getIBAN(), this.account.getBalance());
    }

    @Test(expected = BankException.class)
    public void negativeAmount() {
        Bank.processPayment(this.account.getIBAN(), -100);
    }


    @After
    public void tearDown() {
        Bank.banks.clear();
    }

}
