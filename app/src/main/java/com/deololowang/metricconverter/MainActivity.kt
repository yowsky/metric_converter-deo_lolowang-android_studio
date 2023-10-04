package com.deololowang.metricconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    lateinit var metrikAutoCompleteTextView: AutoCompleteTextView
    lateinit var satuan1AutoCompleteTextView: AutoCompleteTextView
    lateinit var satuan2AutoCompleteTextView: AutoCompleteTextView
    lateinit var satuan1TextInputLayout: TextInputLayout
    lateinit var satuan2TextInputLayout: TextInputLayout
    lateinit var nilaiAwalEditText: EditText
    lateinit var hasilTextView:TextView
    lateinit var rootLayout: LinearLayout

    lateinit var daftarMetrik:Array<String>

    var satuan1: String = ""
    var satuan2: String = ""

    fun initComponents(){
        metrikAutoCompleteTextView = findViewById(R.id.metrikAutoCompleteTextView)
        satuan1AutoCompleteTextView = findViewById(R.id.satuan1AutoCompleteTextView)
        satuan2AutoCompleteTextView = findViewById(R.id.satuan2AutoCompleteTextView)
        nilaiAwalEditText = findViewById(R.id.nilaiAwalEditText)
        hasilTextView = findViewById(R.id.hasilTextView)
        satuan1TextInputLayout = findViewById(R.id.satuan1TextInputLayout)
        satuan2TextInputLayout = findViewById(R.id.satuan2TextInputLayout)
        rootLayout = findViewById(R.id.rootLayout)
    }

    fun initListener(){
        rootLayout.setOnClickListener {
            currentFocus?.clearFocus()
        }

        metrikAutoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()

            if(selectedItem == daftarMetrik[0]) setSatuanAdapter(R.array.select_panjang_item)
            if(selectedItem == daftarMetrik[1]) setSatuanAdapter(R.array.select_massa_item)
            if(selectedItem == daftarMetrik[2]) setSatuanAdapter(R.array.select_waktu_item)
            if(selectedItem == daftarMetrik[3]) setSatuanAdapter(R.array.select_arus_listrik_item)
            if(selectedItem == daftarMetrik[4]) setSatuanAdapter(R.array.select_suhu_item)
            if(selectedItem == daftarMetrik[5]) setSatuanAdapter(R.array.select_intensitas_cahaya_item)
            if(selectedItem == daftarMetrik[6]) setSatuanAdapter(R.array.select_jumlah_zat_item)

            metrikAutoCompleteTextView.clearFocus()
            resetAllInput()
            toggleSatuanSelect(true)
        }

        satuan1AutoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            satuan1AutoCompleteTextView.clearFocus()
            val selectedItem = parent.getItemAtPosition(position).toString()
            satuan1 = selectedItem

            if(satuan2AutoCompleteTextView.text.toString().isNotEmpty() && nilaiAwalEditText.text.toString().isNotEmpty()){
                convertMetrik(nilaiAwalEditText.text.toString().toDouble())
            }

        }

        satuan2AutoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            satuan2AutoCompleteTextView.clearFocus()
            val selectedItem = parent.getItemAtPosition(position).toString()
            satuan2 = selectedItem

            if(satuan1AutoCompleteTextView.text.toString().isNotEmpty() && nilaiAwalEditText.text.toString().isNotEmpty()){
                convertMetrik(nilaiAwalEditText.text.toString().toDouble())
            }
        }

        nilaiAwalEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().isEmpty() || s.toString() == "0") {
                    hasilTextView.visibility = View.GONE
                    return
                }
                if(satuan1.isEmpty() || satuan2.isEmpty()) return
                convertMetrik(s.toString().toDouble())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        /*
        satuan1AutoCompleteTextView.setOnClickListener {
            if(metrikAutoCompleteTextView.text.toString() == "") Toast.makeText(this, "Metrik belum dipilih!", Toast.LENGTH_SHORT).show()
        }

        satuan2AutoCompleteTextView.setOnClickListener {
            if(metrikAutoCompleteTextView.text.toString() == "") Toast.makeText(this, "Metrik belum dipilih!", Toast.LENGTH_SHORT).show()
        }
        */
    }

    fun resetAllInput(){
        satuan1AutoCompleteTextView.setText("")
        satuan2AutoCompleteTextView.setText("")
        nilaiAwalEditText.setText("")
        hasilTextView.setText("")
        hasilTextView.visibility = View.GONE
    }

    fun convertMetrik(nilaiAwal:Double){
        if(satuan1 == satuan2) return setHasil(nilaiAwal,"")

        // m ke km
        if(satuan1 == "Meter(m)" && satuan2 == "Kilometer(km)") return setHasil(nilaiAwal/1000, "km")
        // km ke m
        if(satuan1 == "Kilometer(km)" && satuan2 == "Meter(m)") return setHasil(nilaiAwal*1000, "m")

        // g ke kg
        if(satuan1 == "Gram(g)" && satuan2 == "Kilogram(kg)") return setHasil(nilaiAwal/1000, "kg")
        // kg ke g
        if(satuan1 == "Kilogram(kg)" && satuan2 == "Gram(g)") return setHasil(nilaiAwal*1000, "g")

        // s ke min
        if(satuan1 == "Sekon(s)" && satuan2 == "Menit(min)") return setHasil(nilaiAwal/60, "min")
        // min ke s
        if(satuan1 == "Menit(min)" && satuan2 == "Sekon(s)") return setHasil(nilaiAwal*60, "s")

        // A ke nA
        if(satuan1 == "Ampere(A)" && satuan2 == "Nanoampere(nA)") return setHasil(nilaiAwal*1000000000, "nA")
        // nA ke A
        if(satuan1 == "Nanoampere(nA)" && satuan2 == "Ampere(A)") return setHasil(nilaiAwal/1000000000, "A")

        // A ke nA
        if(satuan1 == "Celcius(C)" && satuan2 == "Fahrenheit(F)") return setHasil((nilaiAwal*9/5)+32, "F")
        // nA ke A
        if(satuan1 == "Fahrenheit(F)" && satuan2 == "Celcius(C)") return setHasil((nilaiAwal-32)*5/9, "C")

        // cd ke lm
        if(satuan1 == "Candela(cd)" && satuan2 == "Lumen(lm)") return setHasil(nilaiAwal*12.56637, "lm")
        // lm ke cd
        if(satuan1 == "Lumen(lm)" && satuan2 == "Candela(cd)") return setHasil(nilaiAwal/12.56637, "cd")

        // mol ke kmol
        if(satuan1 == "Mole(mol)" && satuan2 == "Kilomole(kmol)") return setHasil(nilaiAwal/1000, "kmol")
        // kmol ke mol
        if(satuan1 == "Kilomole(kmol)" && satuan2 == "Mole(mol)") return setHasil(nilaiAwal*1000, "mol")
    }

    fun setHasil(res:Double,prefix:String){
        hasilTextView.text = res.toString().trimEnd('0').trimEnd('.') + " " + prefix
        hasilTextView.visibility = View.VISIBLE
    }

    fun setSatuanAdapter(arrStringRes:Int){
        satuan1AutoCompleteTextView.setAdapter(ArrayAdapter(this,R.layout.dropdown_select_item,resources.getStringArray(arrStringRes)))
        satuan2AutoCompleteTextView.setAdapter(ArrayAdapter(this,R.layout.dropdown_select_item,resources.getStringArray(arrStringRes)))
    }

    fun initMetrikDropdownAdapterAndListener(){
        daftarMetrik = resources.getStringArray(R.array.daftar_metrik)
        val adapter = ArrayAdapter(this,R.layout.dropdown_select_item,daftarMetrik)
        metrikAutoCompleteTextView.setAdapter(adapter)
    }

    fun toggleSatuanSelect(state:Boolean){
        if(!state){
            satuan1TextInputLayout.isEnabled = false
            satuan2TextInputLayout.isEnabled = false
            nilaiAwalEditText.isEnabled = false

            /*
            satuan1AutoCompleteTextView.isFocusableInTouchMode = false
            satuan2AutoCompleteTextView.isFocusableInTouchMode = false

            satuan1AutoCompleteTextView.dropDownHeight = 0
            satuan2AutoCompleteTextView.dropDownHeight = 0
            */

            return
        }
        satuan1TextInputLayout.isEnabled = true
        satuan2TextInputLayout.isEnabled = true
        nilaiAwalEditText.isEnabled = true

        /*
        satuan1AutoCompleteTextView.isFocusableInTouchMode = true
        satuan2AutoCompleteTextView.isFocusableInTouchMode = true

        satuan1AutoCompleteTextView.dropDownHeight = 300
        satuan2AutoCompleteTextView.dropDownHeight = 300
        */
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initComponents()
        initListener()

        // SET SUBTITLE PADA ACTION BAR
        supportActionBar?.setSubtitle("By. Deo Lolowang")

        // GET DAFTAR METRIK ARRAY
        initMetrikDropdownAdapterAndListener()

        // MEMATIKKAN PILIH SATUAN
        toggleSatuanSelect(false)
    }
}