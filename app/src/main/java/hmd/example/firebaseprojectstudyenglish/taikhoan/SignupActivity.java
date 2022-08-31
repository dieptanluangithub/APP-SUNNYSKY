
package hmd.example.firebaseprojectstudyenglish.taikhoan;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import hmd.example.firebaseprojectstudyenglish.R;
import hmd.example.firebaseprojectstudyenglish.database.DatabaseAccess;
import hmd.example.firebaseprojectstudyenglish.singletonpattern.MessageObject;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    TextView tvDangNhap;
    EditText edtHoTen,edtEmail,edtSdt,edtMatKhau,edtXacNhan;
    Button btnSignUp;
    FirebaseAuth mAuth;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    DatabaseAccess DB;
    private MessageObject messageObject = MessageObject.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        AnhXa();
        mAuth = FirebaseAuth.getInstance();

        DB =  DatabaseAccess.getInstance(getApplicationContext());
        tvDangNhap = (TextView) findViewById(R.id.textView_login);
        tvDangNhap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hoten = edtHoTen.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String sdt = edtSdt.getText().toString().trim();
                String matkhau = edtMatKhau.getText().toString().trim();
                String xacnhanmatkhau = edtXacNhan.getText().toString().trim();

                if(hoten.equals("")||email.equals("")||sdt.equals("")||matkhau.equals(""))
                {
                    messageObject.ShowDialogMessage(Gravity.CENTER,
                            SignupActivity.this,
                            "Vui lòng điền đầy đủ thông tin của bạn!!",
                            0);
                }
                else{
                    if(matkhau.equals(xacnhanmatkhau)){

                        Boolean kiemtrataikhoan = DB.checktaikhoan(email);
                        if(kiemtrataikhoan == false)
                        {
                            mAuth.createUserWithEmailAndPassword(email, matkhau).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task)
                                        {
                                            if (task.isSuccessful()) {
                                                DB.open();
                                                Boolean insert = DB.insertData(mAuth.getCurrentUser().getUid(),hoten,email,sdt,0, 1);
                                                DB.close();
                                                btnSignUp.setText(insert.toString());
                                                Toast.makeText(SignupActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();

                                                // if the user created intent to login activity
                                                rootNode= FirebaseDatabase.getInstance();
                                                reference= rootNode.getReference("User");
                                                User newuser = new User(mAuth.getCurrentUser().getUid(), hoten,0,email,sdt, 1);
                                                reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(newuser);

                                                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                                startActivity(intent);
                                            }
                                            else {
                                                messageObject.ShowDialogMessage(Gravity.CENTER,
                                                        SignupActivity.this,
                                                        "Đăng ký thất bại!!",
                                                        0);
                                            }
                                        }
                                    });
                        }
                        else{
                            messageObject.ShowDialogMessage(Gravity.CENTER,
                                    SignupActivity.this,
                                    "Email đã được đăng ký!!",
                                    0);
                        }


                    }
                    else{
                        messageObject.ShowDialogMessage(Gravity.CENTER,
                                SignupActivity.this,
                                "Xác nhận lại, mật khẩu không trùng khớp!!",
                                0);
                        edtMatKhau.setText("");
                        edtXacNhan.setText("");
                    }
                }
            }
        });

    }
    private void AnhXa()
    {
        tvDangNhap = (TextView) findViewById(R.id.textView_login);
        edtHoTen = (EditText) findViewById(R.id.editTextEmailNav);
        edtEmail = (EditText) findViewById(R.id.editTextEmail);
        edtSdt = (EditText) findViewById(R.id.editTextSdt);
        edtMatKhau= (EditText) findViewById(R.id.editTextMatKhau);
        edtXacNhan = (EditText) findViewById(R.id.editTextXacNhan);
        btnSignUp = (Button) findViewById(R.id.buttonSignUp);
    }
}