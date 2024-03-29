package com.example.s27275bank;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BankServiceITest {
    @Autowired
    private BankService bankService;

    @Test
    public void createClientSuccess() {
        Client client = this.bankService.createClient(123.0);
        Client client2 = this.bankService.createClient(546.0);

        assertThat(client.getBalance()).isEqualTo(123.0);
        assertThat(client2.getBalance()).isEqualTo(546.0);
    }

    @Test
    public void makeTransferSuccess() throws Exception {
        Client client = this.bankService.createClient(546.0);

        client = this.bankService.makeTransfer(client.getId(), 150.0);

        assertThat(client.getBalance()).isEqualTo(546.0 - 150.0);
        assertThat(client.getTransfersHistory().toString()).isEqualTo("[Transfer{amount=150.0, transferDirection=OUTGOING, transferStatus=ACCEPTED}]");
    }

    @Test
    public void makeTransferNoClient() {
        try {
            this.bankService.makeTransfer(10, 150.0);
        } catch (Exception exception) {
            assertThat(exception.getMessage()).isEqualTo("Client does not exist");
        }
    }

    @Test
    public void makeTransferNoMoney() {
        Client client = this.bankService.createClient(10.0);

        try {
            this.bankService.makeTransfer(client.getId(), 150.0);
        } catch (Exception exception) {
            assertThat(exception.getMessage()).isEqualTo("Not enough balance");
        }
    }

    @Test
    public void depositMoneySuccess() throws Exception {
        Client client = this.bankService.createClient(500.0);

        client = this.bankService.depositMoney(client.getId(), 150.0);

        assertThat(client.getBalance()).isEqualTo(500.0 + 150.0);
        assertThat(client.getTransfersHistory().toString()).isEqualTo("[Transfer{amount=150.0, transferDirection=INCOMING, transferStatus=ACCEPTED}]");
    }

    @Test
    public void depositMoneyNoClient() {
        try {
            this.bankService.depositMoney(10, 150.0);
        } catch (Exception exception) {
            assertThat(exception.getMessage()).isEqualTo("Client does not exist");
        }
    }

    @Test
    public void getClientDataSuccess() throws Exception {
        Client clientCreated = this.bankService.createClient(500.0);
        Client client = this.bankService.getClientData(clientCreated.getId());

        assertThat(clientCreated.getId()).isEqualTo(client.getId());
        assertThat(clientCreated.getBalance()).isEqualTo(client.getBalance());
    }

    @Test
    public void getClientDataNoClient() {
        try {
            this.bankService.getClientData(10);
        } catch (Exception exception) {
            assertThat(exception.getMessage()).isEqualTo("Client does not exist");
        }
    }
}